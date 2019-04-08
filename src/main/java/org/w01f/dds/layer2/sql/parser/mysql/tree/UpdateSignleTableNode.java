package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class UpdateSignleTableNode extends UpdateNode implements Cloneable {

    private TableNameAndAliasNode tableNameAndAlias;
    private SetExprsNode setExprs;
    private WhereConditionNode whereCondition;
    private IntPlaceHolderNode rowCount;

    @Override
    public UpdateSignleTableNode clone() {
        TableNameAndAliasNode tableNameAndAliasNode = tableNameAndAlias == null ? null : tableNameAndAlias.clone();
        SetExprsNode setExprsNode = setExprs == null ? null : setExprs.clone();
        WhereConditionNode whereConditionNode = whereCondition == null ? null : whereCondition.clone();
        IntPlaceHolderNode intPlaceHolderNode = rowCount == null ? null : rowCount.clone();
        return new UpdateSignleTableNode(tableNameAndAliasNode, setExprsNode, whereConditionNode, intPlaceHolderNode);
    }

    public UpdateSignleTableNode(TableNameAndAliasNode tableNameAndAlias, SetExprsNode setExprs, WhereConditionNode whereCondition, IntPlaceHolderNode rowCount) {
        this.tableNameAndAlias = tableNameAndAlias;
        this.setExprs = setExprs;
        this.whereCondition = whereCondition;
        this.rowCount = rowCount;

        setParent(tableNameAndAlias, setExprs, whereCondition, rowCount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(this.tableNameAndAlias).append(" set ").append(this.setExprs.toString());
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

    public SetExprsNode getSetExprs() {
        return setExprs;
    }

    public WhereConditionNode getWhereCondition() {
        return whereCondition;
    }

    public IntPlaceHolderNode getRowCount() {
        return rowCount;
    }
}
