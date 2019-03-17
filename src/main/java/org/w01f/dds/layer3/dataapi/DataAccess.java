package org.w01f.dds.layer3.dataapi;

import java.sql.ResultSet;

public class DataAccess implements IDataAccess {
    @Override
    public void insert(String sql, Object[] values) {

    }

    @Override
    public int delete(String sql, String[] ids) {
        return 0;
    }

    @Override
    public int delete(String sql, String id) {
        return 0;
    }

    @Override
    public int update(String sql, Object[] values, String id) {
        return 0;
    }

    @Override
    public int update(String sql, Object[] values, String[] ids) {
        return 0;
    }

    @Override
    public ResultSet select(String sql, String id) {
        return null;
    }

    @Override
    public ResultSet select(String sql, String[] ids) {
        return null;
    }
}
