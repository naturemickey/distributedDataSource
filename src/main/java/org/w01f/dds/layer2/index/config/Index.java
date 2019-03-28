package org.w01f.dds.layer2.index.config;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Index {

    private static String indexDelimiter = "/";

    private Table table;
    private Column[] columns;
    private boolean unique = false;

    public Index(Table table, Column[] columns) {
        this.table = table;
        this.columns = columns;

        this.table.indices.add(this);
    }

    public Index(Table table, Column[] columns, boolean unique) {
        this(table, columns);
        this.unique = unique;
    }

    public boolean isUnique() {
        return unique;
    }

    public Table getTable() {
        return table;
    }

    public Column[] getColumns() {
        return columns;
    }

    private String name;

    public String getName() {
        if (name == null) {
            name = table.getName() + "." + Arrays.asList(columns).stream().map(Column::getName).collect(Collectors.joining(indexDelimiter));
        }
        return name;
    }
}
