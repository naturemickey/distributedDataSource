package org.w01f.dds.service;

import org.apache.log4j.spi.Configurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w01f.dds.layer1.id.IDConfig;
import org.w01f.dds.layer1.id.IDGenerator;
import org.w01f.dds.layer2.index.IndexConfigUtils;
import org.w01f.dds.layer2.index.config.Column;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;
import org.w01f.dds.service.biz.ITestBiz;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

public class TestServiceBean {

    public static void main(String[] args) throws Exception {

        Table table = new Table("ttt");
        new Index(table, new Column[]{new Column("name", Column.Type.VARCHAR1000)});
        IndexConfigUtils.parseConfig(new Table[]{table});

        new IDConfig("0");

        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
        //DataSource sqliteDS = (DataSource) ac.getBean("dataSource");
        //sqliteDS.getConnection().createStatement().execute("create table ttt(id varchar(100), name varchar(100))");
        ITestBiz testBiz = ac.getBean(ITestBiz.class);

        String id = IDGenerator.takeId();
        //id = "0000005e6cyyzec90wgPzJJ4fwt8ROPGCu7";
        testBiz.testInsert(id, "test_name1");
        System.out.println(testBiz.testSelet(id));
        testBiz.testUpdate(id, "test_name2");
        System.out.println(testBiz.testSelet(id));
        testBiz.testDelete(id);
        System.out.println(testBiz.testSelet(id));
    }


    static class Test {
        String id;
    }
}
