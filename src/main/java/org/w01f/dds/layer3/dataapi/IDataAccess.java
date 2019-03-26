package org.w01f.dds.layer3.dataapi;

import java.sql.ResultSet;
import java.util.Map;

public interface IDataAccess {
    void insert(String tableName, Map<String, Object> valueMap);

    int delete(String tableName, String[] ids);

    int delete(String tableName, String id);

    int update(String tableName, Map<String, Object> valueMap, String id);

    int update(String tableName, Map<String, Object> valueMap, String[] ids);

    ResultSet select(String tableName, String id);

    ResultSet select(String tableName, String[] ids);

}
