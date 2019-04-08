package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;

public class TableJoinNode extends TableRelNode  implements Cloneable {

	private final TableNameAndAliasNode table;
	private final TableJoinSuffixNode suffix;

	@Override
	public TableJoinNode clone() {
		return new TableJoinNode(table.clone(), suffix.clone());
	}

	public TableJoinNode(TableNameAndAliasNode table, TableJoinSuffixNode suffix) {
		this.table = table;
		this.suffix = suffix;

		setParent(table, suffix);
	}

	@Override
	public String toString() {
		return table + " " + suffix;
	}

	@Override
	public List<TableAndJoinMod> getRealTables() {
		List<TableAndJoinMod> res = new ArrayList<>();
		res.add(new TableAndJoinMod(table));
		res.addAll(suffix.getRealTables());
		return res;
	}

}
