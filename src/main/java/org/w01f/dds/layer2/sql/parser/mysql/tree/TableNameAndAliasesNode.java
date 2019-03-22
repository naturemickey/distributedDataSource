package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TableNameAndAliasesNode extends SQLSyntaxTreeNode implements Cloneable {

    private List<TableNameAndAliasNode> tableNameAndAlias;

    @Override
    public TableNameAndAliasesNode clone() {
        List<TableNameAndAliasNode> tableNameAndAlias = null;
        if (this.tableNameAndAlias != null) {
            tableNameAndAlias = new ArrayList<>(this.tableNameAndAlias.size());
            for (TableNameAndAliasNode nameAndAlias : this.tableNameAndAlias) {
                tableNameAndAlias.add(nameAndAlias.clone());
            }
        }
        return new TableNameAndAliasesNode(tableNameAndAlias);
    }

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
