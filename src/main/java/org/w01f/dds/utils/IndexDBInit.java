package org.w01f.dds.utils;

import org.w01f.dds.layer2.index.IndexConfigUtils;
import org.w01f.dds.layer2.index.config.Column;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;
import org.w01f.dds.layer2.sql.utils.SQLBuildUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IndexDBInit {

    public static void main(String[] args) throws Exception {
        Table table = new Table("ttt");
        new Index(table, new Column[]{new Column("name", Column.Type.VARCHAR1000)});
        IndexConfigUtils.parseConfig(new Table[]{table});

        Set<String> sqlSet = new HashSet<>();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:db/indexdb.sqlitedb");) {

            for (Index index : IndexConfigUtils.allIndex()) {

                for (int i = 0; i < SlotConsistentHashing.getSlotcount(); i++) {
                    List<String> tableCreate = SQLBuildUtils.sql4CreateIndexTable(index, i);

                    for (String sql : tableCreate) {
                        if (!sqlSet.contains(sql)) {
                            sqlSet.add(sql);
                            try (Statement statement = connection.createStatement()) {
                                statement.execute(sql);
                            }
                        }
                    }
                }
            }
        }
    }
}
