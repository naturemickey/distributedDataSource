package org.w01f.dds.layer2.index;

import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;

import java.util.HashMap;
import java.util.Map;

public class IndexStructureUtils {

    // tableName --> indexName --> is(or not)unique
//    private static Map<String, Map<String, Boolean>> indexMap = new HashMap<>();
//
//    public static void parseIndexConfig(Table[] tables) {
//        for (Table table : tables) {
//            if (table.getIndices() != null) {
//                for (Index index : table.getIndices()) {
//                    makeCache(index, index.isUnique());
//                }
//            }
//        }
//    }
//
//    private static void makeCache(Index index, boolean isUnique) {
//        String tableName = index.getTable().getName();
//        String indexName = index.getName();
//        Map<String, Boolean> idxMap = indexMap.get(tableName);
//        if (idxMap == null) {
//            idxMap = new HashMap<>();
//            indexMap.put(tableName, idxMap);
//        }
//        if (idxMap.containsKey(indexName)) {
//            throw new RuntimeException("index names have been repeated.");
//        }
//        idxMap.put(indexName, isUnique);
//    }

    private static Map<String, Table> tableMap = new HashMap<>();

    public static void parseConfig(Table[] tables) {
        for (Table table : tables) {
            tableMap.put(table.getName().toUpperCase(), table);
        }
    }

    public static Table getTableConfig(String tableName) {
        return tableMap.get(tableName.toUpperCase());
    }
}
