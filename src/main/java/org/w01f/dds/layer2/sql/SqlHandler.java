package org.w01f.dds.layer2.sql;

import org.w01f.dds.layer1.dsproxy.param.Param;
import org.w01f.dds.layer1.id.IDGenerator;
import org.w01f.dds.layer2.index.IndexConfigUtils;
import org.w01f.dds.layer2.index.config.Column;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.sql.parser.mysql.tree.*;
import org.w01f.dds.layer2.sql.utils.SQLbreakUtil;
import org.w01f.dds.layer3.dataapi.IDataAccess;
import org.w01f.dds.layer3.indexapi.IIndexAccess;
import org.w01f.dds.layer4.data.DataAccess;
import org.w01f.dds.layer4.index.IndexAccess;
import org.w01f.dds.layer4.index.SQLBuildUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SqlHandler {

    private IIndexAccess indexAccess = new IndexAccess();
    private IDataAccess dataAccess = new DataAccess();

    public int executeUpdate(StatNode statNode) {
        if (statNode.isInsert()) {
            return handleInsert(statNode);
        } else if (statNode.isUpdate()) {
            return handleUpdate(statNode);
        } else if (statNode.isDelete()) {
            return handleDelete(statNode);
        } else {
            throw new RuntimeException("it is impossible");
        }
    }

    private int handleInsert(StatNode statNode) {
        InsertNode insertNode = statNode.getDmlAsInsert();
        Map<Index, Param[]> indexMap = SQLbreakUtil.parseInsertIndex(insertNode);

        this.indexAccess.insert(indexMap);

        final List<ElementPlaceholderNode> placeholderNodes = statNode.getPlaceholderNodes();
        final List<String> names = insertNode.getColumnNames().getNames();
        final ElementPlaceholderNode idHolder = placeholderNodes.get(names.indexOf("id"));
        final String id = idHolder.getParam().getValue()[1].toString();

        return dataAccess.executeUpdate(statNode, IDGenerator.getDbNo(id));
    }

    public ResultSet executeQuery(StatNode statNode) {
        final SelectNode selectNode = statNode.getDmlAsSelect();
        final SelectInner selectInner = selectNode.getSelectInner();

        if (selectNode.getSuffix() != null) {
            // TODO 未来支持
            throw new RuntimeException("not support union syntax : " + statNode);
        }

        final SelectPrefixNode prefix = selectInner.getPrefix();
        final SelectSuffixNode suffix = selectInner.getSuffix();
        final TablesNode tables = prefix.getTables();
        final List<TableRelNode.TableAndJoinMod> realTables = tables.getRealTables();
        if (realTables.size()>1) {
            throw new RuntimeException("not support multi-table select : " + statNode);
        }

        return null;
    }

    private int handleUpdate(StatNode statNode) {
        // this method just like 'handleDelete' method. very very like.
        UpdateNode un = statNode.getDmlAsUpdate();

        if (un instanceof UpdateMultipleTableNode) {
            throw new RuntimeException("not support mutiple table update sentence : " + statNode.toString());
        }

        final UpdateSignleTableNode updateNode = (UpdateSignleTableNode) un;

        final SetExprsNode setExprs = updateNode.getSetExprs();
        final TableNameAndAliasNode tableNameAndAlias = updateNode.getTableNameAndAlias();
        final WhereConditionNode whereCondition = updateNode.getWhereCondition();
        final IntPlaceHolderNode rowCount = updateNode.getRowCount();

        if (rowCount == null) {
            throw new RuntimeException("update sentence not support limit word right now : " + statNode.toString());
        }

        final String tableName = tableNameAndAlias.getName();
        final List<List<ExpressionNode>> wheres = SQLbreakUtil.breakWhere(whereCondition);
        final List<Index> indices = IndexConfigUtils.getTableConfig(tableName).getIndices();

        int sum = 0;
        for (List<ExpressionNode> andWhere : wheres) {
            final Index index = SQLbreakUtil.chooseIndex(andWhere, indices);

            if (index == null) {
                final List<String> ids = SQLbreakUtil.getIds(andWhere);
                if (ids.isEmpty()) {
                    noIndexThrow(tableName, andWhere);
                } else {
                    Set<Integer> dbNos = ids.stream().map(IDGenerator::getDbNo).collect(Collectors.toSet());

                    sum += dbNos.stream().mapToInt(dbNo -> this.dataAccess.executeUpdate(statNode, dbNo)).sum();
                }
            } else {
                final List<ExpressionNode> newDeleteWhereNodes = new ArrayList<>();
                final List<ExpressionNode> newIndexWhereNodes = new ArrayList<>();

                for (int i = 0; i < index.getColumns().length; i++) {
                    final Column column = index.getColumns()[i];

                    final ExpressionNode expression = getExpression(column.getName(), andWhere, i);
                    if (expression != null) {
                        newIndexWhereNodes.add(expression);
                    }
                }

                newDeleteWhereNodes.addAll(andWhere);

                final StatNode selectIndexNode = SQLBuildUtils.sql4QueryIndex(index, newIndexWhereNodes);
                ResultSet idRs = indexAccess.query(selectIndexNode);

                try {
                    // dbNo -> id set
                    Map<Integer, Set<String>> idMap = getIntegerSetMap(idRs);

                    for (Map.Entry<Integer, Set<String>> entry : idMap.entrySet()) {
                        final Integer dbNo = entry.getKey();
                        final Set<String> idSet = entry.getValue();

                        final ElementTextNode idElement = new ElementTextNode("id");
                        final ValueListNode valueListNode = new ValueListNode(idSet.stream().map(ElementPlaceholderNode::new).collect(Collectors.toList()));
                        final ExpressionInValuesNode expressionInValuesNode = new ExpressionInValuesNode(idElement, valueListNode);


                        WhereConditionOpNode whereConditionNode = new WhereConditionOpNode(expressionInValuesNode);
                        for (ExpressionNode whereNode : newDeleteWhereNodes) {
                            whereConditionNode = new WhereConditionOpNode(whereNode, "and", whereConditionNode);
                        }

                        final UpdateNode newUpdateNode = new UpdateSignleTableNode(tableNameAndAlias, setExprs, whereConditionNode);

                        sum += this.dataAccess.executeUpdate(new StatNode(newUpdateNode), dbNo);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sum;
    }

    private int handleDelete(StatNode statNode) {
        // this method just like 'handleUpdate' method. very very like.
        final DeleteNode deleteNode = statNode.getDmlAsDelete();

        final TableNameAndAliasNode tableNameAndAlias = deleteNode.getTableNameAndAlias();
        final WhereConditionNode whereCondition = deleteNode.getWhereCondition();
        final IntPlaceHolderNode rowCount = deleteNode.getRowCount();

        if (rowCount != null) {
            throw new RuntimeException("delete sentence not support limit word right now : " + statNode.toString());
        }

        final String tableName = tableNameAndAlias.getName();
        final List<List<ExpressionNode>> wheres = SQLbreakUtil.breakWhere(whereCondition);
        final List<Index> indices = IndexConfigUtils.getTableConfig(tableName).getIndices();

        int sum = 0;
        for (List<ExpressionNode> andWhere : wheres) {
            final Index index = SQLbreakUtil.chooseIndex(andWhere, indices);

            if (index == null) {
                final List<String> ids = SQLbreakUtil.getIds(andWhere);
                if (ids.isEmpty()) {
                    noIndexThrow(tableName, andWhere);
                } else {
                    Set<Integer> dbNos = ids.stream().map(IDGenerator::getDbNo).collect(Collectors.toSet());

                    sum += dbNos.stream().mapToInt(dbNo -> this.dataAccess.executeUpdate(statNode, dbNo)).sum();
                }
            } else {
                final List<ExpressionNode> newDeleteWhereNodes = new ArrayList<>();
                final List<ExpressionNode> newIndexWhereNodes = new ArrayList<>();

                for (int i = 0; i < index.getColumns().length; i++) {
                    final Column column = index.getColumns()[i];

                    final ExpressionNode expression = getExpression(column.getName(), andWhere, i);
                    if (expression != null) {
                        newIndexWhereNodes.add(expression);
                    }
                }

                newDeleteWhereNodes.addAll(andWhere);

                final StatNode selectIndexNode = SQLBuildUtils.sql4QueryIndex(index, newIndexWhereNodes);
                ResultSet idRs = indexAccess.query(selectIndexNode);

                try {
                    // dbNo -> id set
                    Map<Integer, Set<String>> idMap = getIntegerSetMap(idRs);

                    for (Map.Entry<Integer, Set<String>> entry : idMap.entrySet()) {
                        final Integer dbNo = entry.getKey();
                        final Set<String> idSet = entry.getValue();

                        final ElementTextNode idElement = new ElementTextNode("id");
                        final ValueListNode valueListNode = new ValueListNode(idSet.stream().map(ElementPlaceholderNode::new).collect(Collectors.toList()));
                        final ExpressionInValuesNode expressionInValuesNode = new ExpressionInValuesNode(idElement, valueListNode);


                        WhereConditionOpNode whereConditionNode = new WhereConditionOpNode(expressionInValuesNode);
                        for (ExpressionNode whereNode : newDeleteWhereNodes) {
                            whereConditionNode = new WhereConditionOpNode(whereNode, "and", whereConditionNode);
                        }

                        final DeleteNode newDeleteNode = new DeleteNode(tableNameAndAlias, whereConditionNode);

                        sum += this.dataAccess.executeUpdate(new StatNode(newDeleteNode), dbNo);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sum;
    }

    private Map<Integer, Set<String>> getIntegerSetMap(ResultSet idRs) throws SQLException {
        Map<Integer, Set<String>> idMap = new HashMap<>();
        while (idRs.next()) {
            final String id = idRs.getString("id");
            final int dbNo = IDGenerator.getDbNo(id);

            idMap.compute(dbNo, (no, set) -> {
                if (set == null) {
                    set = new HashSet<>();
                }
                set.add(id);
                return set;
            });
        }
        return idMap;
    }

    private void noIndexThrow(String tableName, List<ExpressionNode> andWhere) {
        final StringBuilder sb = new StringBuilder("from ").append(tableName).append(" where");
        for (ExpressionNode expressionNode : andWhere) {
            sb.append(" ").append(expressionNode).append(" and ");
        }
        sb.delete(sb.length() - 5, sb.length());
        throw new RuntimeException("there is no index match: " + sb);
    }

    private ExpressionNode getExpression(String columnName, List<ExpressionNode> andWhere, int columnNo) {
        for (int i = 0; i < andWhere.size(); i++) {
            ExpressionNode expressionNode = andWhere.get(i);
            final ExpressionNode newExpressionNode = convertToNewWhere(columnName, expressionNode, columnNo);
            if (newExpressionNode != null) {
                andWhere.remove(i);

                return newExpressionNode;
            }
        }
        return null;
    }

    private ExpressionNode convertToNewWhere(String columnName, ExpressionNode expressionNode, int columnNo) {
        // only support these 4 types:
        if (expressionNode instanceof ExpressionRelationalNode) {
            final String op = ((ExpressionRelationalNode) expressionNode).getRelationalOp();
            final ElementNode left = ((ExpressionRelationalNode) expressionNode).getLeft();
            final ElementNode right = ((ExpressionRelationalNode) expressionNode).getRight();

            boolean isFirst = columnNo == 0;
            if ((isFirst && op.equals("=")) || !isFirst) {
                final ElementTextNode node = SQLbreakUtil.getElementTextNode(((ExpressionRelationalNode) expressionNode));
                if (node != null) {
                    if (SQLbreakUtil.textMatchColumn(columnName, node.getTxt())) {
                        final ElementTextNode elementTextNode = new ElementTextNode("v" + columnNo);
                        if (left == node) {
                            return new ExpressionRelationalNode(elementTextNode, right, op);
                        } else {
                            return new ExpressionRelationalNode(left, elementTextNode, op);
                        }
                    }
                }
            }
        } else if (expressionNode instanceof ExpressionBetweenAndNode) {
            final ElementNode element = ((ExpressionBetweenAndNode) expressionNode).getElement();
            final boolean not = ((ExpressionBetweenAndNode) expressionNode).isNot();
            final ElementNode left = ((ExpressionBetweenAndNode) expressionNode).getLeft();
            final ElementNode right = ((ExpressionBetweenAndNode) expressionNode).getRight();

            if (element instanceof ElementTextNode) {
                if (SQLbreakUtil.textMatchColumn(columnName, ((ElementTextNode) element).getTxt())) {
                    final ElementTextNode elementTextNode = new ElementTextNode("v" + columnNo);
                    return new ExpressionBetweenAndNode(elementTextNode, not, left, right);
                }
            }
        } else if (expressionNode instanceof ExpressionInValuesNode) {
            final ElementNode element = ((ExpressionInValuesNode) expressionNode).getElement();
            final boolean not = ((ExpressionInValuesNode) expressionNode).isNot();
            final ValueListNode values = ((ExpressionInValuesNode) expressionNode).getValues();

            if (element instanceof ElementTextNode) {
                if (SQLbreakUtil.textMatchColumn(columnName, ((ElementTextNode) element).getTxt())) {
                    final ElementTextNode elementTextNode = new ElementTextNode("v" + columnNo);
                    return new ExpressionInValuesNode(elementTextNode, not, values);
                }
            }
        } else if (expressionNode instanceof ExpressionLikeNode) {
            final ElementNode left = ((ExpressionLikeNode) expressionNode).getLeft();
            final boolean not = ((ExpressionLikeNode) expressionNode).isNot();
            final ElementNode right = ((ExpressionLikeNode) expressionNode).getRight();

            if (left instanceof ElementTextNode) {
                if (SQLbreakUtil.textMatchColumn(columnName, ((ElementTextNode) left).getTxt())) {
                    final ElementTextNode elementTextNode = new ElementTextNode("v" + columnNo);
                    return new ExpressionLikeNode(not, elementTextNode, right);
                }
            }
        }
        return null;
    }

    public boolean execute(StatNode statNode) {
        executeUpdate(statNode);
        return true;
    }
}
