package org.w01f.dds.service.dao;

public interface ITestDao {

    void testInsert(String id, String name);
    void testUpdate(String id, String name);
    void testDelete(String id);
    String testSelet(String id);

}
