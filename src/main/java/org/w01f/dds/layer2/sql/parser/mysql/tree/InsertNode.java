package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class InsertNode extends SQLSyntaxTreeNode  implements Cloneable {

	private String tableName;
	private ColumnNamesNode columnNames;
	private ValueListNode valueNames;
	private SelectNode select;

	@Override
	public InsertNode clone() {
		ColumnNamesNode columnNamesNode = columnNames == null ? null : columnNames.clone();
		if (valueNames != null)
			return new InsertNode(tableName, columnNamesNode, valueNames.clone());
		return new InsertNode(tableName, columnNamesNode, select.clone());
	}

	public InsertNode(String tableName, ColumnNamesNode columnNames, ValueListNode valueNames) {
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.valueNames = valueNames;
	}

	public InsertNode(String tableName, ColumnNamesNode columnNames, SelectNode select) {
		super();
		this.tableName = tableName;
		this.columnNames = columnNames;
		this.select = select;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("insert into ");
		sb.append(tableName);
		if (columnNames != null) {
			sb.append(" (").append(columnNames.toString()).append(')');
		}
		if (valueNames != null) {
			sb.append(" values (").append(valueNames.toString()).append(')');
		}
		if (select != null) {
			sb.append(' ').append(select);
		}
		return sb.toString();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ColumnNamesNode getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(ColumnNamesNode columnNames) {
		this.columnNames = columnNames;
	}

	public ValueListNode getValueNames() {
		return valueNames;
	}

	public void setValueNames(ValueListNode valueNames) {
		this.valueNames = valueNames;
	}

	public SelectNode getSelect() {
		return select;
	}

	public void setSelect(SelectNode select) {
		this.select = select;
	}

}
