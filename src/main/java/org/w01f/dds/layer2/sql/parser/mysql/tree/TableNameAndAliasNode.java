package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;

public class TableNameAndAliasNode extends TableRelNode implements Cloneable {

    private final String name;
    private final String alias;

    @Override
    public TableNameAndAliasNode clone() {
        return new TableNameAndAliasNode(name, alias);
    }

    public TableNameAndAliasNode(String name) {
        this.name = name;
        this.alias = null;
    }

    public TableNameAndAliasNode(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    @Override
    public String toString() {
        if (alias != null)
            return name + ' ' + alias;
        else
            return name;
    }

    @Override
    public List<TableRelNode.TableAndJoinMod> getRealTables() {
        List<TableRelNode.TableAndJoinMod> res = new ArrayList<>();
        res.add(new TableRelNode.TableAndJoinMod(this));
        return res;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}
