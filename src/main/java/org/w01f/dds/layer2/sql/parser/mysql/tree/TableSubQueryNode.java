package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;

public class TableSubQueryNode extends TableRelNode implements Cloneable {

    private final SelectNode select;
    private final String alias;

    @Override
    public TableSubQueryNode clone() {
        return new TableSubQueryNode(select == null ? null : select.clone(), alias);
    }

    public TableSubQueryNode(SelectNode select, String alias) {
        this.select = select;
        this.alias = alias;

        setParent(select);
    }

    @Override
    public String toString() {
        return "(" + select + ") " + alias;
    }

    @Override
    public List<TableAndJoinMod> getRealTables() {
        return new ArrayList<>();
    }
}
