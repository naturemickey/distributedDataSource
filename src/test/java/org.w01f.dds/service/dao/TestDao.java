package org.w01f.dds.service.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TestDao implements ITestDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void testInsert(String id, String name) {
        this.jdbcTemplate.update("insert into ttt(id, name) values(?, ?)", id, name);
        //this.jdbcTemplate.update("insert into ttt(id, name) values(\"" + id + "\", \"" + name + "\")");
    }

    @Override
    public void testUpdate(String id, String name) {
        this.jdbcTemplate.update("update ttt set name = ? where id = ?", name, id);
        // this.jdbcTemplate.update("update ttt set name = \"" + name + "\" where id = \"" + id + "\"");
    }

    @Override
    public void testDelete(String id) {
        this.jdbcTemplate.update("delete from ttt where id = ?", id);
        //this.jdbcTemplate.update("delete from ttt where id = \"" + id + "\"");
    }

    @Override
    public String testSelet(String id) {
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList("select name from ttt where id = ?", id);
        if (list.size() > 0) {
            return (String) list.get(0).get("name");
        }
        return null;
    }

    @Override
    public String testSeletByName(String name) {
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList("select name from ttt where name = ?", name);
        if (list.size() > 0) {
            return (String) list.get(0).get("name");
        }
        return null;
    }
}
