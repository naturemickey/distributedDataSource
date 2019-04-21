package org.w01f.dds.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TestDao implements ITestDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void testInsert(String id, String name) {
        final String sql = "insert into ttt(id, name) values(?, ?)";
        System.out.println("original sql : " + sql);
        this.jdbcTemplate.update(sql, id, name);
    }

    @Override
    public void testUpdate(String id, String name) {
        final String sql = "update ttt set name = ? where id = ?";
        System.out.println("original sql : " + sql);
        this.jdbcTemplate.update(sql, name, id);
    }

    @Override
    public void testDelete(String id) {
        final String sql = "delete from ttt where id = ?";
        System.out.println("original sql : " + sql);
        this.jdbcTemplate.update(sql, id);
    }

    @Override
    public String testSelet(String id) {
        final String sql = "select name from ttt where id = ?";
        System.out.println("original sql : " + sql);
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, id);
        if (list.size() > 0) {
            return (String) list.get(0).get("name");
        }
        return null;
    }

    @Override
    public String testSeletByName(String name) {
        final String sql = "select name from ttt where name = ?";
        System.out.println("original sql : " + sql);
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, name);
        if (list.size() > 0) {
            return (String) list.get(0).get("name");
        }
        return null;
    }
}
