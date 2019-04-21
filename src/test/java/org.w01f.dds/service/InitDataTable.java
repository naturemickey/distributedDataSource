package org.w01f.dds.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

public class InitDataTable {

    public static void main(String[] args) throws Exception {

        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
        DataSource testDataDS1 = (DataSource) ac.getBean("testDataDS1");
        testDataDS1.getConnection().createStatement().execute("create table ttt(id varchar(100) primary key, name varchar(100))");
        DataSource testDataDS2 = (DataSource) ac.getBean("testDataDS2");
        testDataDS2.getConnection().createStatement().execute("create table ttt(id varchar(100) primary key, name varchar(100))");

    }
}
