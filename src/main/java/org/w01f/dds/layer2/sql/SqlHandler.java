package org.w01f.dds.layer2.sql;

import org.w01f.dds.layer1.dsproxy.param.Param;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlHandler {

    private IIndexAccess indexAccess = new IndexAccess();
    private IDataAccess dataAccess = new DataAccess();

    public ResultSet executeQuery(StatNode statNode) {
        return null;
    }

    public int executeUpdate(StatNode statNode) {
        if (statNode.isInsert()) {
            InsertNode insertNode = statNode.getDmlAsInsert();
            Map<Index, Param[]> indexMap = SQLbreakUtil.parseInsertIndex(insertNode);

            this.indexAccess.insert(indexMap);

            // TODO: need add logic for data insert.
        } else if (statNode.isUpdate()) {
            UpdateNode updateNode = statNode.getDmlAsUpdate();
        } else if (statNode.isDelete()) {
            DeleteNode deleteNode = statNode.getDmlAsDelete();

            final String tableName = deleteNode.getTableNameAndAlias().getName();
            final List<List<ExpressionNode>> wheres = SQLbreakUtil.breakWhere(deleteNode.getWhereCondition());
            final Map<Index, List<ExpressionNode>> indexMap = SQLbreakUtil.chooseIndics(wheres, IndexConfigUtils.getTableConfig(tableName).getIndices());

            for (Map.Entry<Index, List<ExpressionNode>> entry : indexMap.entrySet()) {
                final Index index = entry.getKey();
                final List<ExpressionNode> andWhere = entry.getValue();
                final List<ExpressionNode> newDeleteWhereNodes = new ArrayList<>();
                final List<ExpressionNode> newIndexWhereNodes = new ArrayList<>();

                boolean isFirst = true;
                for (Column column : index.getColumns()) {
                    final ExpressionNode expression = getExpression(column.getName(), andWhere, isFirst);
                    if (expression != null) {
                        newIndexWhereNodes.add(expression);
                    }

                    isFirst = false;
                }

                newDeleteWhereNodes.addAll(andWhere);

                final StatNode selectIndexNode = SQLBuildUtils.sql4QueryIndex(index, newIndexWhereNodes);
                ResultSet idRs = indexAccess.query(selectIndexNode);
            }
        }

        return 1;
    }

    private ExpressionNode getExpression(String columnName, List<ExpressionNode> andWhere, boolean isFirst) {
        for (int i = 0; i < andWhere.size(); i++) {
            ExpressionNode expressionNode = andWhere.get(i);
            if (isMatch(columnName, expressionNode, isFirst)) {
                andWhere.remove(i);

                return expressionNode;
            }
        }
        return null;
    }

    private boolean isMatch(String columnName, ExpressionNode expressionNode, boolean isFirst) {
        // only support these 4 types:
        if (expressionNode instanceof ExpressionRelationalNode) {
            final String op = ((ExpressionRelationalNode) expressionNode).getRelationalOp();
            if ((isFirst && op.equals("=")) || !isFirst) {
                final ElementTextNode node = SQLbreakUtil.getElementTextNode(((ExpressionRelationalNode) expressionNode));
                if (node != null) {
                    if (SQLbreakUtil.textMatchColumn(columnName, node.getTxt())) {
                        return true;
                    }
                }
            }
        } else if (expressionNode instanceof ExpressionBetweenAndNode) {
            final ElementNode element = ((ExpressionBetweenAndNode) expressionNode).getElement();
            if (element instanceof ElementTextNode) {
                if (SQLbreakUtil.textMatchColumn(columnName, ((ElementTextNode) element).getTxt())) {
                    return true;
                }
            }
        } else if (expressionNode instanceof ExpressionInValuesNode) {
            final ElementNode element = ((ExpressionInValuesNode) expressionNode).getElement();
            if (element instanceof ElementTextNode) {
                if (SQLbreakUtil.textMatchColumn(columnName, ((ElementTextNode) element).getTxt())) {
                    return true;
                }
            }
        } else if (expressionNode instanceof ExpressionLikeNode) {
            final ElementNode element = ((ExpressionLikeNode) expressionNode).getLeft();
            if (element instanceof ElementTextNode) {
                if (SQLbreakUtil.textMatchColumn(columnName, ((ElementTextNode) element).getTxt())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean execute(StatNode statNode) {
        executeUpdate(statNode);
        return true;
    }
}
