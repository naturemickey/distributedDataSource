package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionRelationalNode extends ExpressionNode implements Cloneable {
    private final ElementNode left;
    private final ElementNode right;
    private final String relationalOp;

    @Override
    public ExpressionRelationalNode clone() {
        ElementNode leftNode = left == null ? null : left.clone();
        ElementNode rightNode = right == null ? null : right.clone();
        return new ExpressionRelationalNode(leftNode, rightNode, relationalOp);
    }

    public ExpressionRelationalNode(ElementNode left, ElementNode right, String relationalOp) {
        this.left = left;
        this.right = right;
        this.relationalOp = relationalOp;

        setParent(left, right);
    }

    @Override
    public String toString() {
        return this.left.toString() + ' ' + this.relationalOp + ' ' + this.right;
    }

    public ElementNode getLeft() {
        return left;
    }

    public ElementNode getRight() {
        return right;
    }

    public String getRelationalOp() {
        return relationalOp;
    }
}
