package org.w01f.dds.layer2.index.config;

import java.util.Arrays;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return getName().equals(index.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
