package org.w01f.dds.layer2.sql.mysql.tree;

public class ExpressionNotNode extends ExpressionNode {

	private ExpressionNode expression;

	public ExpressionNotNode(ExpressionNode expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "not " + expression;
	}

}
