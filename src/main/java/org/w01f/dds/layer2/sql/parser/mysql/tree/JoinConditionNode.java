package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class JoinConditionNode extends SQLSyntaxTreeNode  implements Cloneable  {

	private final WhereConditionNode on;
	private final ColumnNamesNode columnNames;

	@Override
	public JoinConditionNode clone() {
		WhereConditionNode whereConditionNode = on == null ? null : on.clone();
		ColumnNamesNode columnNamesNode = columnNames == null ? null : columnNames.clone();
		return new JoinConditionNode(whereConditionNode, columnNamesNode);
	}

	public JoinConditionNode(WhereConditionNode on, ColumnNamesNode columnNames) {
		this.on = on;
		this.columnNames = columnNames;

		setParent(on, columnNames);
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
