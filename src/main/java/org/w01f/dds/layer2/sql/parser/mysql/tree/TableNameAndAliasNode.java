package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;

public class TableNameAndAliasNode extends TableRelNode implements Cloneable {

    private String name;
    private String alias;

    @Override
    public TableNameAndAliasNode clone() {
        return new TableNameAndAliasNode(name, alias);
    }

    public TableNameAndAliasNode(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<TableRelNode.TableAndJoinMod> getRealTables() {
        List<TableRelNode.TableAndJoinMod> res = new ArrayList<>();
        res.add(new TableRelNode.TableAndJoinMod(this));
        return res;
    }

}
