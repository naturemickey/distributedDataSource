package org.w01f.dds.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w01f.dds.layer2.index.IndexConfigUtils;
import org.w01f.dds.layer2.index.config.Column;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;
import org.w01f.dds.layer2.sql.utils.SQLBuildUtils;
import org.w01f.dds.utils.SlotConsistentHashing;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InitIndexDB {

    public static void main(String[] args) throws Exception {
        Table table = new Table("ttt");
        new Index(table, new Column[]{new Column("name", Column.Type.VARCHAR1000)});
        IndexConfigUtils.parseConfig(new Table[]{table});

        Set<String> sqlSet = new HashSet<>();

        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
        DataSource testIndexDS1 = (DataSource) ac.getBean("testIndexDS1");
        DataSource testIndexDS2 = (DataSource) ac.getBean("testIndexDS2");

        Connection[] connections = {testIndexDS1.getConnection(), testIndexDS2.getConnection()};

        for (Index index : IndexConfigUtils.allIndex()) {

            for (int i = 0, len = SlotConsistentHashing.getSlotcount(); i < len; i++) {
                List<String> tableCreate = SQLBuildUtils.sql4CreateIndexTable(index, i);

                for (String sql : tableCreate) {
                    if (!sqlSet.contains(sql)) {
                        sqlSet.add(sql);

                        int x = (i < len / 2) ? 0 : 1;

                        try (Statement statement = connections[x].createStatement()) {
                            statement.execute(sql);
                        }
                    }
                }
            }
        }

    }
}
