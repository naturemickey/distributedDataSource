package org.w01f.dds.layer5;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DataSourceProxy {

    public DataSourceProxy(String dbName) {
        openedConn.set(new HashMap<>());

        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite:db/" + dbName);

        dsMap.put(0, ds);
    }

    private Map<Integer, DataSource> dsMap = new HashMap<>();
    private ThreadLocal<Map<Integer, Connection>> openedConn = new ThreadLocal<>();

    public Connection getConnection(Integer dbNo) throws SQLException {
        DataSource dataSource = dsMap.get(dbNo);

        Map<Integer, Connection> connectionMap = openedConn.get();
        Connection connection = connectionMap.get(dbNo);
        if (connection == null) {
            connection = dataSource.getConnection();
            connectionMap.put(dbNo, connection);
        }

        return connection;
    }

    void commitAll() throws SQLException {
        Map<Integer, Connection> connections = openedConn.get();
        try {
            for (Connection connection : connections.values()) {
                connection.commit();
            }
        } finally {
            for (Connection connection : connections.values()) {
                connection.close();
            }
            connections.clear();
        }
    }

    void rollbackAll() throws SQLException {
        Map<Integer, Connection> connections = openedConn.get();
        try {
            for (Connection connection : connections.values()) {
                connection.rollback();
            }
        } finally {
            for (Connection connection : connections.values()) {
                connection.close();
            }
            connections.clear();
        }
    }

}
