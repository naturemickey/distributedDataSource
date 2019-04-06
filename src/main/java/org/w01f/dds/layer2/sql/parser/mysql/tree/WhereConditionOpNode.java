package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class WhereConditionOpNode extends WhereConditionNode implements Cloneable {

    private ExpressionNode expression;
    private String expressionOperator;
    private WhereConditionNode whereCondition;

    @Override
    public WhereConditionOpNode clone() {
        ExpressionNode expressionNode = expression == null ? null : expression.clone();
        WhereConditionNode whereConditionNode = whereCondition == null ? null : whereCondition.clone();
        return new WhereConditionOpNode(expressionNode, expressionOperator, whereConditionNode);
    }

    public WhereConditionOpNode(ExpressionNode expression, String expressionOperator, WhereConditionNode whereCondition) {
        this.expression = expression;
        this.expressionOperator = expressionOperator == null ? null : expressionOperator.toLowerCase();
        this.whereCondition = whereCondition;

        setParent(expression, whereCondition);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(expression.toString());
        if (this.expressionOperator != null && this.whereCondition != null) {
            sb.append(' ').append(this.expressionOperator).append(' ').append(this.whereCondition);
        }
        return sb.toString();
    }

    public ExpressionNode getExpression() {
        return expression;
    }

    public String getExpressionOperator() {
        return expressionOperator;
    }

    public WhereConditionNode getWhereCondition() {
        return whereCondition;
    }
}
