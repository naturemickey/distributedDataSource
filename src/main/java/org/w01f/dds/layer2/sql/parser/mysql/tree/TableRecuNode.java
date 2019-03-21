package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.List;

public class TableRecuNode extends TableRelNode implements Cloneable  {

	private TableRelNode tableRel;

	@Override
	public TableRecuNode clone() {
		return new TableRecuNode(tableRel.clone());
	}

	public TableRecuNode(TableRelNode tableRel) {
		this.tableRel = tableRel;
	}

	@Override
	public String toString() {
		return "(" + tableRel + ")";
	}

	public TableRelNode getTableRel() {
		return tableRel;
	}

	public void setTableRel(TableRelNode tableRel) {
		this.tableRel = tableRel;
	}

	@Override
	public List<TableRelNode.TableAndJoinMod> getRealTables() {
		return this.tableRel.getRealTables();
	}

}
