package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.List;

public abstract class TableRelNode extends SQLSyntaxTreeNode  implements Cloneable  {

	public abstract List<TableAndJoinMod> getRealTables();

	public static class TableAndJoinMod {
		private TableRelNode tableRelNode;
		private String tableJoinMod;

		public TableAndJoinMod(TableRelNode tableNameAndAliasNode) {
			this.tableRelNode = tableNameAndAliasNode;
		}

		public TableAndJoinMod(TableRelNode tableNameAndAliasNode, String tableJoinMod) {
			this.tableRelNode = tableNameAndAliasNode;
			this.tableJoinMod = tableJoinMod;
		}

		public TableRelNode getTableRelNode() {
			return tableRelNode;
		}

		public String getTableJoinMod() {
			return tableJoinMod;
		}
	}

	@Override public TableRelNode clone(){
		throw new RuntimeException("need be override.");
	}

}
