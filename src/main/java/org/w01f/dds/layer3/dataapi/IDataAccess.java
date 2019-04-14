package org.w01f.dds.layer3.dataapi;

import org.w01f.dds.layer2.sql.parser.mysql.tree.StatNode;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public interface IDataAccess {
//    void insert(String tableName, Map<String, Object> valueMap);

    void execute(StatNode statNode, int dbNo);

    int executeUpdate(StatNode statNode, int dbNo);

    Supplier<ResultSet> executeQuery(StatNode statNode, int dbNo);

    DatabaseMetaData getMetaData() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

//    int delete(String tableName, String[] ids);
//
//    int delete(String tableName, String id);
//
//    int update(String tableName, Map<String, Object> valueMap, String id);
//
//    int update(String tableName, Map<String, Object> valueMap, String[] ids);
//
//    ResultSet select(String tableName, String id);
//
//    ResultSet select(String tableName, String[] columns, String id);
//
//    ResultSet select(String tableName, String[] ids);
//
//    ResultSet select(String tableName, String[] columns, String[] ids);

}
