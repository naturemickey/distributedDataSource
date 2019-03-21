package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TablesNode extends SQLSyntaxTreeNode implements Cloneable  {

	private List<TableRelNode> tableRels;

	@Override
	public TablesNode clone() {
		List<TableRelNode> tableRels = null;
		if (this.tableRels != null) {
			tableRels = new ArrayList<>(this.tableRels.size()) ;
			for (TableRelNode tableRel : this.tableRels) {
				tableRels.add(tableRel.clone());
			}
		}
		return new TablesNode(tableRels);
	}

	public TablesNode(List<TableRelNode> tableRels) {
		this.tableRels = tableRels;
	}

	@Override
	public String toString() {
		return tableRels.stream().map(tr -> tr.toString()).collect(Collectors.joining(", "));
	}

	public List<TableRelNode.TableAndJoinMod> getRealTables() {
		return tableRels.stream().flatMap(tr -> tr.getRealTables().stream()).collect(Collectors.toList());
	}
}
