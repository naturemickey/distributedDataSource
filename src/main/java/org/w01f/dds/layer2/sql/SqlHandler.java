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
import org.w01f.dds.layer5.ResultSetProxy;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SqlHandler {

    private IIndexAccess indexAccess;
    private IDataAccess dataAccess;

    public void setIndexAccess(IIndexAccess indexAccess) {
        this.indexAccess = indexAccess;
    }

    public void setDataAccess(IDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return this.dataAccess.getMetaData();
    }

    public void commit() throws SQLException {
        dataAccess.commit();
        indexAccess.commit();
    }

    public void rollback() throws SQLException {
        indexAccess.rollback();
        dataAccess.rollback();
    }

    public void close() throws SQLException {
        dataAccess.close();
        indexAccess.close();
    }

    public boolean execute(StatNode statNode) {
        executeUpdate(statNode);
        return true;
    }

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

    public ResultSet executeQuery(StatNode statNode) {
        final SelectNode selectNode = statNode.getDmlAsSelect();
        final SelectInner selectInner = selectNode.getSelectInner();

        if (selectNode.getSuffix() != null) {
            // TODO 未来支持 union all，不考虑支持 union distinct
            throw new RuntimeException("not support union syntax : " + statNode);
        }

        final SelectPrefixNode prefix = selectInner.getPrefix();
        final SelectSuffixNode suffix = selectInner.getSuffix();

        final TablesNode tables = prefix.getTables();
        final SelectExprsNode selectExprs = prefix.getSelectExprs();
        final WhereConditionNode whereCondition = prefix.getWhere();
        final GbobExprsNode groupByExprs = prefix.getGroupByExprs();
        final boolean distinct = prefix.isDistinct();
        final WhereConditionNode having = prefix.getHaving();

        final IntPlaceHolderNode rowCount = suffix == null ? null : suffix.getRowCount();
        final String lock = suffix == null ? null : suffix.getLock();
        final IntPlaceHolderNode offset = suffix == null ? null : suffix.getOffset();
        final GbobExprsNode orderByExprs = suffix == null ? null : suffix.getOrderByExprs();
        // final boolean hasOffsetWord = suffix.isHasOffsetWord();

        final List<TableRelNode.TableAndJoinMod> realTables = tables.getRealTables();
        if (realTables.size() > 1) {
            throw new RuntimeException("not support multi-table select : " + statNode);
        }
        if (groupByExprs != null) { // TODO 未来可考虑提供有限的支持：对有索引的列进行group操作。
            throw new RuntimeException("not support 'group by' syntax : " + statNode);
        }
        if (orderByExprs != null) { // TODO 未来可考虑提供有限的支持：对有索引的列进行order操作。
            throw new RuntimeException("not support 'order by' syntax : " + statNode);
        }
        if (distinct) {
            throw new RuntimeException("not support distinct word : " + statNode);
        }
        if (having != null) { // TODO 未来可考虑提供有限的支持：对有索引的列进行having操作。
            throw new RuntimeException("not support 'having' syntax : " + statNode);
        }
        if (offset != null || rowCount != null) { // TODO 未来可考虑支持
            throw new RuntimeException("not support 'limit' syntax : " + statNode);
        }
        if (lock != null) {
            throw new RuntimeException("not support '" + lock + "' syntax : " + statNode);
        }
        final TableNameAndAliasNode tableNameAndAlias = realTables.get(0).getTableNameAndAliasNode();
        final String tableName = tableNameAndAlias.getName();

        final List<List<ExpressionNode>> wheres = SQLbreakUtil.breakWhere(whereCondition);
        final List<Index> indices = IndexConfigUtils.getTableConfig(tableName).getIndices();

        for (List<ExpressionNode> andWhere : wheres) {
            final Index index = SQLbreakUtil.chooseIndex(andWhere, indices);

            if (index == null) {
                final List<String> ids = SQLbreakUtil.getIds(andWhere);
                if (ids.isEmpty()) {
                    noIndexThrow(tableName, andWhere);
                } else {
                    Set<Integer> dbNos = ids.stream().map(IDGenerator::getDbNo).collect(Collectors.toSet());

                    final List<Supplier<ResultSet>> suppliers = dbNos.stream().map(dbNo -> this.dataAccess.executeQuery(statNode, dbNo)).collect(Collectors.toList());

                    return new ResultSetProxy(suppliers).getProxy();
                }
            } else {
                final List<ExpressionNode> newDeleteWhereNodes = new ArrayList<>();
                final List<ExpressionNode> newIndexWhereNodes = new ArrayList<>();

                ExpressionRelationalNode slotExp = null;
                for (int i = 0; i < index.getColumns().length; i++) {
                    final Column column = index.getColumns()[i];

                    final ExpressionNode expression = getExpression(column.getName(), andWhere, i);
                    if (expression != null) {
                        newIndexWhereNodes.add(expression);
                    }

                    if (i == 0) {
                        slotExp = ((ExpressionRelationalNode) expression);
                    }
                }

                newDeleteWhereNodes.addAll(andWhere);

                String slotValue = ((ElementPlaceholderNode) slotExp.getRight()).getParam().getValue().toString();

                ResultSet idRs = indexAccess.query(index, slotValue, newIndexWhereNodes);

                try {
                    // dbNo -> id set
                    Map<Integer, Set<String>> idMap = getIntegerSetMap(idRs);

                    List<Supplier<ResultSet>> results = new ArrayList<>(idMap.size());

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

                        final SelectPrefixNode newSelectPrefixNode = new SelectPrefixNode(selectExprs, new TablesNode(tableNameAndAlias), whereConditionNode);
                        final SelectInner newSelectInner = new SelectInner(newSelectPrefixNode);
                        final SelectNode newSelectNode = new SelectNode(newSelectInner);
                        final StatNode newStatNode = new StatNode(newSelectNode);

                        results.add(this.dataAccess.executeQuery(newStatNode, dbNo));
                    }

                    return new ResultSetProxy(results).getProxy();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        throw new RuntimeException("impossible to run until here.");
    }

    private int handleInsert(StatNode statNode) {
        InsertNode insertNode = statNode.getDmlAsInsert();
        Map<Index, Param[]> indexMap = SQLbreakUtil.parseInsertIndex(insertNode);

        this.indexAccess.insert(indexMap);

        final List<ElementPlaceholderNode> placeholderNodes = statNode.getPlaceholderNodes();
        final List<String> names = insertNode.getColumnNames().getNames();
        final ElementPlaceholderNode idHolder = placeholderNodes.get(names.indexOf("id"));
        final String id = idHolder.getParam().getValue().toString();

        return dataAccess.executeUpdate(statNode, IDGenerator.getDbNo(id));
    }

    private Map<String, Object> getRow(ResultSet resultSet) {
        Map<String, Object> map = new HashMap<>();
        try {
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                final String key = metaData.getColumnName(i);
                final Object val = resultSet.getObject(i);

                map.put(key, val);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private Map<String, Object> getSetData(SetExprsNode setExprsNode) {
        Map<String, Object> map = new HashMap<>();

        for (SetExprNode setExpr : setExprsNode.getSetExprs()) {
            final ElementNode left = setExpr.getLeft();
            final ElementNode right = setExpr.getRight();

            if (left instanceof ElementTextNode && right instanceof ElementPlaceholderNode) {
                final String name = ((ElementTextNode) left).getTxt();
                final Object value = ((ElementPlaceholderNode) right).getParam().getValue();

                map.put(name, value);
            } else {
                throw new RuntimeException("not support non-placeholder set : " + setExprsNode);
            }
        }
        return map;
    }

    private final Object o = new Object();

    private int updateData(TableNameAndAliasNode tableNameAndAlias, ResultSet resultSet, SetExprsNode setExprsNode, List<Index> indices) {
        try {
            int sum = 0;
            while (resultSet.next()) {
                final Map<String, Object> row = getRow(resultSet);
                final Map<String, Object> setData = getSetData(setExprsNode);
                final String id = row.get("id").toString();

                for (Index index : indices) {
                    Object[] oldIndex = new Object[index.getColumns().length];
                    Object[] newIndex = new Object[index.getColumns().length];

                    for (int i = 0; i < index.getColumns().length; i++) {
                        Column column = index.getColumns()[i];
                        oldIndex[i] = row.get(column.getName());
                        final Object val = setData.getOrDefault(column.getName(), o);
                        if (val != o) {
                            newIndex[i] = val;
                        }
                    }

                    if (!Arrays.equals(oldIndex, newIndex)) {
                        final String value = oldIndex[0].toString(); // todo : -> dbNo

                        this.indexAccess.delete(index, id, oldIndex);
                        this.indexAccess.insert(index, id, newIndex);
                    }
                }
                final ElementTextNode left = new ElementTextNode("id");
                final ElementPlaceholderNode right = new ElementPlaceholderNode("?");
                final Param param = new Param("setString", new Object[]{1, id});
                right.setParam(param);
                final ExpressionRelationalNode expressionRelationalNode = new ExpressionRelationalNode(left, right, "=");
                final WhereConditionOpNode where = new WhereConditionOpNode(expressionRelationalNode);
                final UpdateSignleTableNode update = new UpdateSignleTableNode(tableNameAndAlias, setExprsNode, where);

                sum += this.dataAccess.executeUpdate(new StatNode(update), IDGenerator.getDbNo(id));
            }
            return sum;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int handleUpdate(StatNode statNode) {
        UpdateNode un = statNode.getDmlAsUpdate();

        if (un instanceof UpdateMultipleTableNode) {
            throw new RuntimeException("not support mutiple table update sentence : " + statNode.toString());
        }

        final UpdateSignleTableNode updateNode = (UpdateSignleTableNode) un;

        final SetExprsNode setExprs = updateNode.getSetExprs();
        final TableNameAndAliasNode tableNameAndAlias = updateNode.getTableNameAndAlias();
        final WhereConditionNode whereCondition = updateNode.getWhereCondition();
        final IntPlaceHolderNode rowCount = updateNode.getRowCount();

        if (rowCount != null) {
            throw new RuntimeException("update sentence not support limit word right now : " + statNode.toString());
        }
        final String tableName = tableNameAndAlias.getName();
        final List<Index> indices = IndexConfigUtils.getTableConfig(tableName).getIndices();

        final StatNode select = new StatNode(new SelectNode(tableNameAndAlias, whereCondition));
        final ResultSet resultSet = this.executeQuery(select);

        return this.updateData(tableNameAndAlias, resultSet, setExprs, indices);
    }

    private int delete(TableNameAndAliasNode tableNameAndAlias, ResultSet resultSet, List<Index> indices) {
        try {
            int sum = 0;
            while (resultSet.next()) {
                final Map<String, Object> row = getRow(resultSet);
                final String id = row.get("id").toString();

                // delete indices of current row.
                for (Index index : indices) {
                    final String indexName = index.getName();

                    Object[] oldIndex = new Object[index.getColumns().length];

                    for (int i = 0; i < index.getColumns().length; i++) {
                        Column column = index.getColumns()[i];
                        oldIndex[i] = row.get(column.getName());
                    }

                    this.indexAccess.delete(index, id, oldIndex);
                }

                final ElementTextNode left = new ElementTextNode("id");
                final ElementPlaceholderNode right = new ElementPlaceholderNode("?");
                final Param param = new Param("setString", new Object[]{1, id});
                right.setParam(param);
                final ExpressionRelationalNode expressionRelationalNode = new ExpressionRelationalNode(left, right, "=");
                final WhereConditionOpNode where = new WhereConditionOpNode(expressionRelationalNode);
                final DeleteNode delete = new DeleteNode(tableNameAndAlias, where);

                sum += this.dataAccess.executeUpdate(new StatNode(delete), IDGenerator.getDbNo(id));
            }
            return sum;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        final List<Index> indices = IndexConfigUtils.getTableConfig(tableName).getIndices();

        final StatNode select = new StatNode(new SelectNode(tableNameAndAlias, whereCondition));
        final ResultSet resultSet = this.executeQuery(select);

        return this.delete(tableNameAndAlias, resultSet, indices);
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
                            return new ExpressionRelationalNode(elementTextNode, left, op);
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
}
