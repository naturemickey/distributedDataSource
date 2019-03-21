package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class UpdateSignleTableNode extends UpdateNode {

	private TableNameAndAliasNode tableNameAndAlias;
	private SetExprsNode setExprs;
	private WhereConditionNode whereCondition;
	private IntPlaceHolderNode rowCount;

	public UpdateSignleTableNode(TableNameAndAliasNode tableNameAndAlias, SetExprsNode setExprs, WhereConditionNode whereCondition, IntPlaceHolderNode rowCount) {
		this.tableNameAndAlias = tableNameAndAlias;
		this.setExprs = setExprs;
		this.whereCondition = whereCondition;
		this.rowCount = rowCount;
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

	public void setTableNameAndAlias(TableNameAndAliasNode tableNameAndAlias) {
		this.tableNameAndAlias = tableNameAndAlias;
	}

	public SetExprsNode getSetExprs() {
		return setExprs;
	}

	public void setSetExprs(SetExprsNode setExprs) {
		this.setExprs = setExprs;
	}

	public WhereConditionNode getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(WhereConditionNode whereCondition) {
		this.whereCondition = whereCondition;
	}

}
