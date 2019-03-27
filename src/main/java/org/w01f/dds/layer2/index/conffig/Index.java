package org.w01f.dds.layer2.index.conffig;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Index {

    private static String indexDelimiter = "/";

    private Table table;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    private String[] columns;

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String getName() {
        return table.getName() + "." + Arrays.asList(columns).stream().collect(Collectors.joining(indexDelimiter));
    }
}
