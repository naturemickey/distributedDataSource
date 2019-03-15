package org.w01f.dds.layer2.index;

public class Table {

    private String name;
    private Unique[] uniques;
    private Index[] indices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unique[] getUniques() {
        return uniques;
    }

    public void setUniques(Unique[] uniques) {
        this.uniques = uniques;
    }

    public Index[] getIndices() {
        return indices;
    }

    public void setIndices(Index[] indices) {
        this.indices = indices;
    }
}
