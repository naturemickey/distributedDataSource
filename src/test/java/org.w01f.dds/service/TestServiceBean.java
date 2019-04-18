package org.w01f.dds.service;

import org.apache.log4j.BasicConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w01f.dds.layer1.id.IDConfig;
import org.w01f.dds.layer1.id.IDGenerator;
import org.w01f.dds.layer2.index.IndexConfigUtils;
import org.w01f.dds.layer2.index.config.Column;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;
import org.w01f.dds.service.biz.ITestBiz;

public class TestServiceBean {

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();

        Table table = new Table("ttt");
        new Index(table, new Column[]{new Column("name", Column.Type.VARCHAR1000)});
        IndexConfigUtils.parseConfig(new Table[]{table});

        new IDConfig("0");

        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");

        ITestBiz testBiz = ac.getBean(ITestBiz.class);

        String id = IDGenerator.takeId();

        System.out.println("\ninsert data[id = " + id + "]:");
        testBiz.testInsert(id, "test_name1");

        System.out.println("\nupdate data:");
        testBiz.testUpdate(id, "test_name2");
        //System.out.println(testBiz.testSelet(id));

        System.out.println("\nselect data[id = " + id + "]:");
        System.out.println(testBiz.testSelet(id));

        System.out.println("\nselect data[name = test_name2]:");
        System.out.println(testBiz.testSeletByName("test_name2"));

        System.out.println("\ndelete data[id = " + id + "]:");
        testBiz.testDelete(id);
    }


    static class Test {
        String id;
    }
}
