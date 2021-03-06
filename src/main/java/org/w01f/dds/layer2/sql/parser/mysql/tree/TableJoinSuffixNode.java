package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableJoinSuffixNode extends SQLSyntaxTreeNode implements Cloneable {
    private final String tableJoinMod;
    private final TableNameAndAliasesNode tables;
    private final TableRecuNode tableRecu;
    private final TableSubQueryNode tableSubQueryNode;
    private final JoinConditionNode condition;
    private final TableJoinSuffixNode suffix;

    @Override
    public TableJoinSuffixNode clone() {
        TableNameAndAliasesNode tableNameAndAliasesNode = tables == null ? null : tables.clone();
        TableRecuNode tableRecuNode = tableRecu == null ? null : tableRecu.clone();
        JoinConditionNode joinConditionNode = condition == null ? null : condition.clone();
        TableJoinSuffixNode tableJoinSuffixNode = suffix == null ? null : suffix.clone();
        TableSubQueryNode tableSubQueryNode = this.tableSubQueryNode == null ? null : this.tableSubQueryNode.clone();
        return new TableJoinSuffixNode(tableJoinMod, tableNameAndAliasesNode, tableRecuNode, tableSubQueryNode, joinConditionNode, tableJoinSuffixNode);
    }

    public TableJoinSuffixNode(String tableJoinMod, TableNameAndAliasesNode tables, TableRecuNode tableRecu, TableSubQueryNode tableSubQueryNode, JoinConditionNode condition,
                               TableJoinSuffixNode suffix) {
        this.tableJoinMod = tableJoinMod;
        this.tables = tables;
        this.tableRecu = tableRecu;
        this.tableSubQueryNode = tableSubQueryNode;
        this.condition = condition;
        this.suffix = suffix;

        setParent(tables, tableRecu, condition, suffix);
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
        if (tableSubQueryNode != null) {
            sb.append(tableSubQueryNode).append(' ');
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
