package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionExistsNode extends ExpressionNode implements Cloneable {

    private final boolean not;
    private final SelectNode select;

    @Override
    public ExpressionExistsNode clone() {
        return new ExpressionExistsNode(not, select == null ? null : select.clone());
    }

    public ExpressionExistsNode(boolean not, SelectNode select) {
        this.not = not;
        this.select = select;

        setParent(select);
    }

    @Override
    public String toString() {
        return (not ? "not exists " : "exists ") + '(' + select + ')';
    }

}
