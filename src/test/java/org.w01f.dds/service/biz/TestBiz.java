package org.w01f.dds.service.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w01f.dds.service.dao.ITestDao;

@Service
public class TestBiz implements ITestBiz {
    @Autowired
    private ITestDao testDao;

    @Override
    public void testInsert(String id, String name) {
        this.testDao.testInsert(id, name);
    }

    @Override
    public void testUpdate(String id, String name) {
        this.testDao.testUpdate(id, name);
    }

    @Override
    public void testDelete(String id) {
        this.testDao.testDelete(id);
    }

    @Override
    public String testSelet(String id) {
        return this.testDao.testSelet(id);
    }
}
