package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionLikeNode extends ExpressionNode implements Cloneable  {
	private boolean not;
	private ElementNode left;
	private ElementNode right;

	@Override
	public ExpressionLikeNode clone() {
		return new ExpressionLikeNode(not, left.clone(), right.clone());
	}

	public ExpressionLikeNode(boolean not, ElementNode left, ElementNode right) {
		this.not = not;
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		String like = not ? " not like " : " like ";
		return left + like + right;
	}

}
