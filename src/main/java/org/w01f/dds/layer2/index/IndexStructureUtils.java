package org.w01f.dds.layer2.index;

import org.w01f.dds.layer2.index.conffig.Index;
import org.w01f.dds.layer2.index.conffig.Table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class IndexStructureUtils {

    // tableName --> indexName --> is(or not)unique
    private static Map<String, Map<String, Boolean>> indexMap = new HashMap<>();

    public static void parseIndexConfig(Table[] tables) {
        for (Table table : tables) {
            if (table.getUniques() != null) {
                for (Index index : table.getUniques()) {
                    makeCache(table, index, true);
                }
            }

            if (table.getIndices() != null) {
                for (Index index : table.getIndices()) {
                    makeCache(table, index, false);
                }
            }
        }
    }

    private static String indexDelimiter = ",";

    private static void makeCache(Table table, Index index, boolean isUnique) {
        String tableName = table.getName();
        String indexName = tableName + "." + Arrays.asList(index.getColumns()).stream().collect(Collectors.joining(indexDelimiter));
        Map<String, Boolean> idxMap = indexMap.get(tableName);
        if (idxMap == null) {
            idxMap = new HashMap<>();
            indexMap.put(tableName, idxMap);
        }
        if (idxMap.containsKey(indexName)) {
            throw new RuntimeException("index names have been repeated.");
        }
        idxMap.put(indexName, isUnique);
    }

//    private static String selectIndex(String tableName, String[] columns) {
//
//    }
}
