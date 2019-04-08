package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class InsertNode extends SQLSyntaxTreeNode implements Cloneable {

    private final String tableName;
    private final ColumnNamesNode columnNames;
    private final ValueListNode valueNames;
    private final SelectNode select;

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
        this.select = null;

        setParent(columnNames, valueNames);
    }

    public InsertNode(String tableName, ColumnNamesNode columnNames, SelectNode select) {
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.valueNames = null;
        this.select = select;

        setParent(columnNames, select);
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

    public ColumnNamesNode getColumnNames() {
        return columnNames;
    }

    public ValueListNode getValueNames() {
        return valueNames;
    }

    public SelectNode getSelect() {
        return select;
    }
}
