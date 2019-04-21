package org.w01f.dds.layer3.dataapi;

import org.w01f.dds.layer2.sql.parser.mysql.tree.StatNode;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public interface IDataAccess {

    void execute(StatNode statNode, int dbNo);

    int executeUpdate(StatNode statNode, int dbNo);

    Supplier<ResultSet> executeQuery(StatNode statNode, int dbNo);

    DatabaseMetaData getMetaData() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

}
