package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableJoinSuffixNode extends SQLSyntaxTreeNode implements Cloneable  {
	private String tableJoinMod;
	private TableNameAndAliasesNode tables;
	private TableRecuNode tableRecu;
	private JoinConditionNode condition;
	private TableJoinSuffixNode suffix;

	@Override
	public TableJoinSuffixNode clone() {
		TableNameAndAliasesNode tableNameAndAliasesNode = tables == null ? null :tables.clone();
		TableRecuNode tableRecuNode = tableRecu == null ?null : tableRecu.clone();
		JoinConditionNode joinConditionNode = condition == null ? null :condition.clone();
		TableJoinSuffixNode tableJoinSuffixNode = suffix == null ? null : suffix.clone();
		return new TableJoinSuffixNode(tableJoinMod, tableNameAndAliasesNode, tableRecuNode, joinConditionNode, tableJoinSuffixNode);
	}

	public TableJoinSuffixNode(String tableJoinMod, TableNameAndAliasesNode tables, TableRecuNode tableRecu, JoinConditionNode condition,
			TableJoinSuffixNode suffix) {
		this.tableJoinMod = tableJoinMod;
		this.tables = tables;
		this.tableRecu = tableRecu;
		this.condition = condition;
		this.suffix = suffix;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.tableJoinMod != null && this.tableJoinMod.length() > 0) {
			sb.append(this.tableJoinMod).append(' ');
		}
		sb.append("join ");
		if (tables != null) {
			int size = tables.all().size();
			if (size == 1) {
				sb.append(tables).append(' ');
			} else {
				sb.append('(').append(tables).append(") ");
			}
		}
		if (tableRecu != null) {
			sb.append(tableRecu).append(' ');
		}
		if (condition != null) {
			sb.append(condition);
		}
		if (suffix != null)
			sb.append(' ').append(suffix);
		return sb.toString();
	}

	public List<TableRelNode.TableAndJoinMod> getRealTables() {
		List<TableRelNode.TableAndJoinMod> res = new ArrayList<>();
		if (tables != null) {
			res.addAll(tables.all().stream().map(tn -> new TableRelNode.TableAndJoinMod(tn, tableJoinMod)).collect(Collectors.toList()));
		}
		if (tableRecu != null) {
			res.addAll(tableRecu.getRealTables());
		}
		if (suffix != null)
			res.addAll(suffix.getRealTables());
		return res;
	}
}
