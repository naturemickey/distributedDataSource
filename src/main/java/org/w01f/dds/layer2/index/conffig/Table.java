package org.w01f.dds.layer2.index.conffig;

/**
 * Every table must has an column named "id" as primary key.
 * If the table has an foreign key, it mast be named "parent_id".
 */
public class Table {

    private String name;
    private Index[] uniques;
    private Index[] indices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Index[] getUniques() {
        return uniques;
    }

    public void setUniques(Index[] uniques) {
        this.uniques = uniques;
    }

    public Index[] getIndices() {
        return indices;
    }

    public void setIndices(Index[] indices) {
        this.indices = indices;
    }
}
