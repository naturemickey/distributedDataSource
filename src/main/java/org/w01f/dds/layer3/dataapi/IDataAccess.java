package org.w01f.dds.layer3.dataapi;

import java.sql.ResultSet;

public interface IDataAccess {
    void insert(String sql, Object[] values);

    int delete(String sql, String[] ids);

    int delete(String sql, String id);

    int update(String sql, Object[] values, String id);

    int update(String sql, Object[] values, String[] ids);

    ResultSet select(String sql, String id);

    ResultSet select(String sql, String[] ids);

}
