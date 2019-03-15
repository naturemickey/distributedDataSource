package org.w01f.dds.service.biz;

public interface ITestBiz {
    void testInsert(String id, String name);
    void testUpdate(String id, String name);
    void testDelete(String id);
    String testSelet(String id);
}
