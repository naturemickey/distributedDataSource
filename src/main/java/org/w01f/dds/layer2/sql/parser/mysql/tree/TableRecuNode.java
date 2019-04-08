package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.List;

public class TableRecuNode extends TableRelNode implements Cloneable  {

	private final TableRelNode tableRel;

	@Override
	public TableRecuNode clone() {
		return new TableRecuNode(tableRel == null ? null : tableRel.clone());
	}

	public TableRecuNode(TableRelNode tableRel) {
		this.tableRel = tableRel;

		setParent(tableRel);
	}

	@Override
	public String toString() {
		return "(" + tableRel + ")";
	}

	public TableRelNode getTableRel() {
		return tableRel;
	}

	@Override
	public List<TableRelNode.TableAndJoinMod> getRealTables() {
		return this.tableRel.getRealTables();
	}

}
