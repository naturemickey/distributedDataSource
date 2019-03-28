package org.w01f.dds.layer2.index.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Every table must has an column named "id" as primary key.
 * If the table has an foreign key, it mast be named "parent_id".
 */
public class Table {

    private String name;
    List<Index> indices = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Index> getIndices() {
        return Collections.unmodifiableList(indices);
    }

}
