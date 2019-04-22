package org.w01f.dds.layer2.sql.utils;

import org.w01f.dds.layer1.dsproxy.param.Param;
import org.w01f.dds.layer2.index.IndexConfigUtils;
import org.w01f.dds.layer2.index.config.Column;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;
import org.w01f.dds.layer2.sql.parser.mysql.tree.*;

import java.util.*;

public class SQLbreakUtil {
    public static Map<Index, Param[]> parseInsertIndex(InsertNode insert) {
        List<String> names = null;
        List<ElementPlaceholderNode> elements = new ArrayList<>();

        { // check insert structure.
            if (insert.getSelect() != null) {
                throw new RuntimeException("only support value insert mode, such as [insert into tab(a, b, c) values(?, ? ,?)]");
            }
            ColumnNamesNode columnNames = insert.getColumnNames();
            if (columnNames != null) {
                names = columnNames.getNames();
            }
            if (names == null || names.size() == 0) {
                throw new RuntimeException("column names cannot be empty.");
            }
            for (ElementNode element : insert.getValueNames().getElements()) {
                if (element instanceof ElementPlaceholderNode) {
                    elements.add(((ElementPlaceholderNode) element));
                } else {
                    throw new RuntimeException("value list only support placeholder list.");
                }
            }
        }

        Table table = IndexConfigUtils.getTableConfig(insert.getTableName());
        List<Index> indices = table.getIndices();
        Map<Index, Param[]> indexSetterMap = new HashMap<>();
        Param idParam = elements.get(names.indexOf("id")).getParam();

        for (Index index : indices) {
            int len = index.getColumns().length;
            Param[] params = new Param[len + 1];
            for (int i = 0; i < len; i++) {
                int si = names.indexOf(index.getColumns()[i].getName());
                params[i] = elements.get(si).getParam();
            }
            params[len] = idParam;

            indexSetterMap.put(index, params);
        }

        return indexSetterMap;
    }

    private static <T> List<List<T>> warpwarpList(T obj) {
        List<List<T>> res = new ArrayList<>(1);
        List<T> l = new ArrayList<>(1);
        l.add(obj);
        res.add(l);
        return res;
    }

    public static List<List<ExpressionNode>> breakWhere(WhereConditionNode whereConditionNode) {
        if (whereConditionNode instanceof WhereConditionOpNode) {
            WhereConditionOpNode node = ((WhereConditionOpNode) whereConditionNode);

            final ExpressionNode expression = node.getExpression();
            final String expressionOperator = node.getExpressionOperator();
            final WhereConditionNode whereCondition = node.getWhereCondition();

            if (expressionOperator == null) {
                return warpwarpList(expression);
            }
            switch (expressionOperator) {
                case "and": {
                    final List<List<ExpressionNode>> wcRes = breakWhere(whereCondition);

                    List<List<ExpressionNode>> result = new ArrayList<>(wcRes.size());

                    for (List<ExpressionNode> wc2re : wcRes) {
                        List<ExpressionNode> list = new ArrayList<>(1 + wc2re.size());
                        list.add(expression);
                        list.addAll(wc2re);

                        result.add(list);
                    }

                    return result;
                }
                case "or": {
                    List<List<ExpressionNode>> result = warpwarpList(expression);
                    result.addAll(breakWhere(whereConditionNode));
                    return result;
                }
                case "xor":
                    throw new RuntimeException("not support \"xor\" keyword.");
                default:
                    throw new RuntimeException("wrond keyword of " + expressionOperator);
            }
        } else {
            WhereConditionSubNode node = ((WhereConditionSubNode) whereConditionNode);
            final String expressionOperator = node.getExpressionOperator();
            final WhereConditionNode wc1 = node.getWc1();
            final WhereConditionNode wc2 = node.getWc2();

            if (expressionOperator == null) {
                return breakWhere(wc1);
            }
            switch (expressionOperator) {
                case "and": {
                    final List<List<ExpressionNode>> wc1res = breakWhere(wc1);
                    final List<List<ExpressionNode>> wc2res = breakWhere(wc2);

                    List<List<ExpressionNode>> result = new ArrayList<>(wc1res.size() * wc2res.size());

                    for (List<ExpressionNode> wc1re : wc1res) {
                        for (List<ExpressionNode> wc2re : wc2res) {
                            List<ExpressionNode> list = new ArrayList<>(wc1re.size() + wc2re.size());

                            list.addAll(wc1re);
                            list.addAll(wc2re);

                            result.add(list);
                        }
                    }

                    return result;
                }
                case "or": {
                    List<List<ExpressionNode>> result = breakWhere(wc1);
                    result.addAll(breakWhere(wc2));
                    return result;
                }
                case "xor":
                    throw new RuntimeException("not support \"xor\" keyword.");
                default:
                    throw new RuntimeException("wrond keyword of " + expressionOperator);
            }
        }
    }

    public static Index chooseIndex(List<ExpressionNode> andWhere, List<Index> indexList) {
        List<Index> indicesRes = new ArrayList<>();
        for (ExpressionNode expressionNode : andWhere) {
            for (Index index : indexList) {
                if (match(expressionNode, index)) {
                    indicesRes.add(index);
                }
            }
        }
        if (indicesRes.size() == 0) {
            return null;
        }
        if (indicesRes.size() == 1) {
            return indicesRes.get(0);
        }

        Index choosed = null;
        for (Index index : indicesRes) {
            choosed = chooseIndex(choosed, index, andWhere);
        }

        return choosed;
    }

    private static Index chooseIndex(Index idx1, Index idx2, List<ExpressionNode> nodes) {
        // 1. return not null;
        if (idx2 == null) {
            return idx1;
        }
        if (idx1 == null) {
            return idx2;
        }
        // 2. return unique
        if (idx1.isUnique()) {
            return idx1;
        }
        if (idx2.isUnique()) {
            return idx2;
        }
        // 3. return more assignment op
        final int count1 = notAssignmentCount(idx1, nodes);
        final int count2 = notAssignmentCount(idx2, nodes);

        if (count1 < count2) {
            return idx1;
        }
        if (count1 > count2) {
            return idx2;
        }
        // 4. return more index columns.
        if (idx1.getColumns().length > idx2.getColumns().length) {
            return idx1;
        }
        if (idx1.getColumns().length < idx2.getColumns().length) {
            return idx2;
        }
        // 5. return anyone.
        return idx1;
    }

    private static int notAssignmentCount(Index index, List<ExpressionNode> nodes) {
        int count = 0;
        for (Column column : index.getColumns()) {
            final String name = column.getName();

            for (ExpressionNode expressionNode : nodes) {
                if (expressionNode instanceof ExpressionRelationalNode) {
                    ElementTextNode node = getElementTextNode(((ExpressionRelationalNode) expressionNode));

                    final String op = ((ExpressionRelationalNode) expressionNode).getRelationalOp();

                    if (node != null) {
                        final String text = node.getTxt();
                        if (textMatchColumn(name, text)) {
                            if (!op.equals("=")) {
                                count += 1;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private static boolean match(ExpressionNode expressionNode, Index index) {
        final Column firstColumn = index.getColumns()[0];
        final String name = firstColumn.getName();
        if (expressionNode instanceof ExpressionRelationalNode) {
            final String op = ((ExpressionRelationalNode) expressionNode).getRelationalOp();

            if (op.equals("=")) {
                ElementTextNode node = getElementTextNode(((ExpressionRelationalNode) expressionNode));

                if (node != null) {
                    final String text = node.getTxt();
                    return textMatchColumn(name, text);
                }
            }
        }
        return false;
    }

    public static ElementTextNode getElementTextNode(ExpressionRelationalNode expressionRelationalNode) {
        final ElementNode left = expressionRelationalNode.getLeft();
        final ElementNode right = expressionRelationalNode.getRight();
        ElementTextNode node = null;
        if (left.getClass().getName().equals(ElementTextNode.class.getName())
                && !right.getClass().getName().equals(ElementTextNode.class.getName())) {
            node = ((ElementTextNode) left);
        } else if (right.getClass().getName().equals(ElementTextNode.class.getName())
                && !left.getClass().getName().equals(ElementTextNode.class.getName())) {
            node = ((ElementTextNode) right);
        }
        return node;
    }

    public static boolean textMatchColumn(String name, String text) {
        if (text.equals(name)) {
            return true;
        }
        if (text.contains(".")) {
            String[] ss = text.split("\\.");
            if (ss.length == 2) {
                return ss[1].equals(name);
            }
        }
        return false;
    }

    public static List<String> getIds(List<ExpressionNode> andWhere) {
        List<String> ids = new ArrayList<>();
        for (ExpressionNode expressionNode : andWhere) {
            if (expressionNode instanceof ExpressionRelationalNode) {
                final String op = ((ExpressionRelationalNode) expressionNode).getRelationalOp();
                final ElementNode left = ((ExpressionRelationalNode) expressionNode).getLeft();
                final ElementNode right = ((ExpressionRelationalNode) expressionNode).getRight();

                if (op.equals("=")) {
                    ElementTextNode maybeIdField = null;
                    ElementPlaceholderNode maybeParamField = null;

                    if (left instanceof ElementTextNode && right instanceof ElementPlaceholderNode) {
                        maybeIdField = ((ElementTextNode) left);
                        maybeParamField = ((ElementPlaceholderNode) right);
                    } else if (right instanceof ElementTextNode && left instanceof ElementPlaceholderNode) {
                        maybeIdField = ((ElementTextNode) right);
                        maybeParamField = ((ElementPlaceholderNode) left);
                    }

                    if (maybeIdField != null && maybeParamField != null && textMatchColumn("id", ((ElementTextNode) maybeIdField).getTxt())) {
                        ids.add(((ElementPlaceholderNode) maybeParamField).getParam().getValue().toString());
                    }
                }
            } else if (expressionNode instanceof ExpressionInValuesNode) {
                final ElementNode element = ((ExpressionInValuesNode) expressionNode).getElement();
                final ValueListNode values = ((ExpressionInValuesNode) expressionNode).getValues();
                final boolean not = ((ExpressionInValuesNode) expressionNode).isNot();

                if (element instanceof ElementTextNode && !not) {
                    if (textMatchColumn("id", ((ElementTextNode) element).getTxt())) {
                        for (ElementNode valuesElement : values.getElements()) {
                            if (valuesElement instanceof ElementPlaceholderNode) {
                                ids.add(((ElementPlaceholderNode) valuesElement).getParam().getValue().toString());
                            } else {
                                throw new RuntimeException("id in () statement must all are placeholder");
                            }
                        }
                    }
                }
            }
        }
        return ids;
    }
}
