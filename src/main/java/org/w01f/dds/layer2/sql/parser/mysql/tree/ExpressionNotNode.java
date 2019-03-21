package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionNotNode extends ExpressionNode implements Cloneable  {

	private ExpressionNode expression;

	@Override
	public ExpressionNotNode clone() {
		return new ExpressionNotNode(expression.clone());
	}

	public ExpressionNotNode(ExpressionNode expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return "not " + expression;
	}

}
