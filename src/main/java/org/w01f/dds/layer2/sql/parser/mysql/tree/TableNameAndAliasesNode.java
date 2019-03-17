package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.List;
import java.util.stream.Collectors;

public class TableNameAndAliasesNode extends SQLSyntaxTreeNode {

	private List<TableNameAndAliasNode> tableNameAndAlias;

	public TableNameAndAliasesNode(List<TableNameAndAliasNode> tableNameAndAlias) {
		this.tableNameAndAlias = tableNameAndAlias;
	}

	@Override
	public String toString() {
		return tableNameAndAlias.stream().map(n -> n.toString()).collect(Collectors.joining(", "));
	}

	public List<TableNameAndAliasNode> all() {
		return tableNameAndAlias;
	}

}
