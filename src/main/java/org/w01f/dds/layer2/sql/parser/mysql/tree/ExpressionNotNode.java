package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionNotNode extends ExpressionNode implements Cloneable {

    private final ExpressionNode expression;

    @Override
    public ExpressionNotNode clone() {
        return new ExpressionNotNode(expression == null ? null : expression.clone());
    }

    public ExpressionNotNode(ExpressionNode expression) {
        this.expression = expression;

        setParent(expression);
    }

    @Override
    public String toString() {
        return "not " + expression;
    }

}
