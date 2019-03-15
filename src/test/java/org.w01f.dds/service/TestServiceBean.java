package org.w01f.dds.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w01f.dds.service.biz.ITestBiz;
import org.w01f.dds.service.biz.TestBiz;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TestServiceBean {

    public static void main(String[] args) throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
        // DataSource sqliteDS = (DataSource) ac.getBean("dataSource");
        // sqliteDS.getConnection().createStatement().execute("create table ttt(id varchar(100), name varchar(100))");
        ITestBiz testBiz = ac.getBean(ITestBiz.class);
        String id = "test_id";
        testBiz.testInsert(id, "test_name1");
        System.out.println(testBiz.testSelet(id));
        testBiz.testUpdate(id, "test_name2");
        System.out.println(testBiz.testSelet(id));
        testBiz.testDelete(id);
        System.out.println(testBiz.testSelet(id));
    }
}
