package org.w01f.dds.utils;

import org.w01f.dds.layer2.index.IndexConfigUtils;
import org.w01f.dds.layer2.index.config.Column;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;
import org.w01f.dds.layer2.sql.utils.SQLBuildUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

public class IndexDBInit {

    public static void main(String[] args) throws Exception {
        Table table = new Table("ttt");
        new Index(table, new Column[]{new Column("name", Column.Type.VARCHAR1000)});
        IndexConfigUtils.parseConfig(new Table[]{table});

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:db/index01.sqlitedb");) {

            for (Index index : IndexConfigUtils.allIndex()) {

                List<String> tableCreate = SQLBuildUtils.sql4CreateIndexTable(index);

                for (String sql : tableCreate) {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(sql);
                    }
                }
            }
        }
    }
}
