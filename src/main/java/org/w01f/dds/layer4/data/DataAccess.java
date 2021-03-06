package org.w01f.dds.layer4.data;

import org.w01f.dds.layer2.sql.parser.mysql.tree.ElementPlaceholderNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.StatNode;
import org.w01f.dds.layer3.dataapi.IDataAccess;
import org.w01f.dds.layer5.DataSourceManager;

import java.sql.*;
import java.util.function.Supplier;

public class DataAccess implements IDataAccess {

    private DataAccess() {
    }

    private static DataAccess instance = new DataAccess();

    public static DataAccess getInstance() {
        return instance;
    }

    private DataSourceManager dataSourceManager = new DataSourceManager();

    public DataSourceManager getDataSourceManager() {
        return dataSourceManager;
    }

    @Override
    public void execute(StatNode statNode, int dbNo) {
        this.executeUpdate(statNode, dbNo);
    }

    @Override
    public int executeUpdate(StatNode statNode, int dbNo) {
        try {
            Connection connection = dataSourceManager.getConnection(dbNo);
            try (PreparedStatement preparedStatement = connection.prepareStatement(statNode.toString());) {
                for (int i = 0; i < statNode.getPlaceholderNodes().size(); i++) {
                    final ElementPlaceholderNode node = statNode.getPlaceholderNodes().get(i);

                    node.getParam().putValue(preparedStatement, i + 1);
                }
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Supplier<ResultSet> executeQuery(StatNode statNode, int dbNo) {
        return () -> {
            try {
                Connection connection = dataSourceManager.getConnection(dbNo);

                PreparedStatement preparedStatement = connection.prepareStatement(statNode.toString());
                for (int i = 0; i < statNode.getPlaceholderNodes().size(); i++) {
                    final ElementPlaceholderNode node = statNode.getPlaceholderNodes().get(i);

                    node.getParam().putValue(preparedStatement, i + 1);
                }

                return preparedStatement.executeQuery();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.dataSourceManager.getFirstConnection().getMetaData();
    }

    @Override
    public void commit() throws SQLException {
        this.dataSourceManager.commitAll();
    }

    @Override
    public void rollback() throws SQLException {
        this.dataSourceManager.rollbackAll();
    }

    @Override
    public void close() throws SQLException {
        this.dataSourceManager.closeAll();
    }

}
