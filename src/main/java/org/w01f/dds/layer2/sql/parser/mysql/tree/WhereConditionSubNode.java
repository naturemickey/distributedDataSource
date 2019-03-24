package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class WhereConditionSubNode extends WhereConditionNode implements Cloneable {

	private WhereConditionNode wc1;
	private String expressionOperator;
	private WhereConditionNode wc2;

	@Override
	public WhereConditionSubNode clone() {
		WhereConditionNode whereConditionNode1 = wc1 == null ? null :wc1.clone();
		WhereConditionNode whereConditionNode2 = wc2 == null ?null :wc2.clone();
		return new WhereConditionSubNode(whereConditionNode1, expressionOperator, whereConditionNode2);
	}

	public WhereConditionSubNode(WhereConditionNode wc1, String expressionOperator, WhereConditionNode wc2) {
		this.wc1 = wc1;
		this.expressionOperator = expressionOperator;
		this.wc2 = wc2;

		setParent(wc1, wc2);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(').append(wc1).append(')');
		if (this.expressionOperator != null && this.wc2 != null) {
			sb.append(' ').append(this.expressionOperator).append(' ').append(this.wc2);
		}
		return sb.toString();
	}

}
