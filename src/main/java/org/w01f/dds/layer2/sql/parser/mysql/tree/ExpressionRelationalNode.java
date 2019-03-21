package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionRelationalNode extends ExpressionNode implements Cloneable {
	private ElementNode left;
	private ElementNode right;
	private String relationalOp;

	@Override
	public ExpressionRelationalNode clone() {
		return new ExpressionRelationalNode(left.clone(), right.clone(), relationalOp);
	}

	public ExpressionRelationalNode(ElementNode left, ElementNode right, String relationalOp) {
		this.left = left;
		this.right = right;
		this.relationalOp = relationalOp;
	}

	@Override
	public String toString() {
		return this.left.toString() + ' ' + this.relationalOp + ' ' + this.right;
	}

}
