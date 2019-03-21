package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class JoinConditionNode extends SQLSyntaxTreeNode  implements Cloneable  {

	private WhereConditionNode on;
	private ColumnNamesNode columnNames;

	@Override
	public JoinConditionNode clone() {
		return new JoinConditionNode(on.clone(), columnNames.clone());
	}

	public JoinConditionNode(WhereConditionNode on, ColumnNamesNode columnNames) {
		this.on = on;
		this.columnNames = columnNames;
	}

	@Override
	public String toString() {
		if (on == null && columnNames == null) {
			return "";
		}
		if (on != null)
			return "on " + on;
		return "using(" + columnNames + ')';
	}

}
