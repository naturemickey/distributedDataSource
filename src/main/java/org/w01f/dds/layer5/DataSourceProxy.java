package org.w01f.dds.layer5;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DataSourceProxy {

    private Map<Integer, DataSource> dsMap = new HashMap<>();
    private ThreadLocal<Map<Integer, Connection>> openedConn = new ThreadLocal<>();

    public void setDsMap(Map<Integer, DataSource> dsMap) {
        this.dsMap = dsMap;
    }

    public Connection getFirstConnection() throws SQLException {
        final Integer no = dsMap.keySet().stream().findFirst().get();
        return getConnection(no);
    }

    public Connection getConnection(Integer dbNo) throws SQLException {
        DataSource dataSource = dsMap.get(dbNo);

        Map<Integer, Connection> connectionMap = openedConn.get();
        if (connectionMap == null) {
            connectionMap = new HashMap<>();
            openedConn.set(connectionMap);
        }
        Connection connection = connectionMap.get(dbNo);
        if (connection == null) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            connectionMap.put(dbNo, connection);
        }

        return connection;
    }

    public void commitAll() throws SQLException {
        Map<Integer, Connection> connections = openedConn.get();
        if (connections != null) {
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
    }

    public void rollbackAll() throws SQLException {
        Map<Integer, Connection> connections = openedConn.get();
        if (connections != null) {
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

}
