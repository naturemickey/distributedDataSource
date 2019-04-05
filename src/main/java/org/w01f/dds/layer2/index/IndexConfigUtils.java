package org.w01f.dds.layer2.index;

import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class IndexConfigUtils {

    private static Map<String, Table> tableMap = new HashMap<>();

    public static void parseConfig(Table[] tables) {
        for (Table table : tables) {
            tableMap.put(table.getName().toUpperCase(), table);
        }
    }

    public static Table getTableConfig(String tableName) {
        return tableMap.get(tableName.toUpperCase());
    }

    public static Collection<Table> allTables() {
        return tableMap.values();
    }

    public static Collection<Index> allIndex() {
        return tableMap.values().stream().flatMap(t -> t.getIndices().stream()).collect(Collectors.toList());
    }
}
