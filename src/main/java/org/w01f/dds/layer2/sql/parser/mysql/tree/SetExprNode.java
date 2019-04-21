package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SetExprNode extends SQLSyntaxTreeNode implements Cloneable {
    private final ElementNode left;
    private final ElementNode right;

    @Override
    public SetExprNode clone() {
        return new SetExprNode(left.clone(), right == null ? null : right.clone());
    }

    public SetExprNode(ElementNode left, ElementNode right) {
        this.left = left;
        this.right = right;

        setParent(left, right);
    }

    @Override
    public String toString() {
        return left.toString() + " = " + (right == null ? "default" : right);
    }

    public ElementNode getLeft() {
        return left;
    }

    public ElementNode getRight() {
        return right;
    }
}
