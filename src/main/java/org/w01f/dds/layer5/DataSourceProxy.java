package org.w01f.dds.layer5;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSourceProxy {

    private Map<Integer, DataSource> dsMap = new HashMap<>();
    private ThreadLocal<List<Connection>> openedConn = new ThreadLocal<>();

    {
        openedConn.set(new ArrayList<>());
    }

    public Connection getConnection(Integer dbNo) throws SQLException {
        DataSource dataSource = dsMap.get(dbNo);
        return dataSource.getConnection();
    }

    public void commitAll() throws SQLException {
        List<Connection> connections = openedConn.get();
        try {
            for (Connection connection : connections) {
                connection.commit();
            }
        } finally {
            for (Connection connection : connections) {
                connection.close();
            }
            connections.clear();
        }
    }

    public void rollbackAll() throws SQLException {
        List<Connection> connections = openedConn.get();
        try {
            for (Connection connection : connections) {
                connection.rollback();
            }
        } finally {
            for (Connection connection : connections) {
                connection.close();
            }
            connections.clear();
        }
    }

}
