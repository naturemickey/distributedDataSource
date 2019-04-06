package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class DeleteNode extends SQLSyntaxTreeNode implements Cloneable {

    private TableNameAndAliasNode tableNameAndAlias;
    private WhereConditionNode whereCondition;
    private IntPlaceHolderNode rowCount;

    @Override
    public DeleteNode clone() {
        TableNameAndAliasNode tableNameAndAliasNode = tableNameAndAlias == null ? null : tableNameAndAlias.clone();
        WhereConditionNode whereConditionNode = whereCondition == null ? null : whereCondition.clone();
        IntPlaceHolderNode intPlaceHolderNode = rowCount == null ? null : rowCount.clone();
        return new DeleteNode(tableNameAndAliasNode, whereConditionNode, intPlaceHolderNode);
    }

    public DeleteNode(TableNameAndAliasNode tableNameAndAlias, WhereConditionNode whereCondition, IntPlaceHolderNode rowCount) {
        this.tableNameAndAlias = tableNameAndAlias;
        this.whereCondition = whereCondition;
        this.rowCount = rowCount;

        super.setParent(tableNameAndAlias, whereCondition, rowCount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("delete from ");
        sb.append(this.tableNameAndAlias.toString());
        if (this.whereCondition != null) {
            sb.append(" where ").append(this.whereCondition.toString());
        }
        if (this.rowCount != null) {
            sb.append(" limit ").append(rowCount);
        }
        return sb.toString();
    }

    public TableNameAndAliasNode getTableNameAndAlias() {
        return tableNameAndAlias;
    }

    public WhereConditionNode getWhereCondition() {
        return whereCondition;
    }

    public IntPlaceHolderNode getRowCount() {
        return rowCount;
    }
}
