package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionLikeNode extends ExpressionNode implements Cloneable  {
	private final boolean not;
	private final ElementNode left;
	private final ElementNode right;

	@Override
	public ExpressionLikeNode clone() {
		ElementNode leftNode = left == null ? null : left.clone();
		ElementNode rightNode = right == null ? null : right.clone();
		return new ExpressionLikeNode(not, leftNode, rightNode);
	}

	public ExpressionLikeNode(boolean not, ElementNode left, ElementNode right) {
		this.not = not;
		this.left = left;
		this.right = right;

		setParent(left, right);
	}

	@Override
	public String toString() {
		String like = not ? " not like " : " like ";
		return left + like + right;
	}

	public boolean isNot() {
		return not;
	}

	public ElementNode getLeft() {
		return left;
	}

	public ElementNode getRight() {
		return right;
	}
}
