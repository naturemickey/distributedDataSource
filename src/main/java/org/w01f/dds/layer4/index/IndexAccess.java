package org.w01f.dds.layer4.index;

import org.w01f.dds.layer1.dsproxy.param.Param;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.sql.parser.mysql.tree.ElementPlaceholderNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.StatNode;
import org.w01f.dds.layer2.sql.utils.SQLBuildUtils;
import org.w01f.dds.layer3.indexapi.IIndexAccess;
import org.w01f.dds.layer5.DataSourceProxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class IndexAccess implements IIndexAccess {

    private DataSourceProxy dataSourceProxy = new DataSourceProxy("indexdb.sqlitedb");

    public void setDataSourceProxy(DataSourceProxy dataSourceProxy) {
        this.dataSourceProxy = dataSourceProxy;
    }

    @Override
    public void insert(Map<Index, Param[]> indexMap) {
        for (Map.Entry<Index, Param[]> entry : indexMap.entrySet()) {
            Index index = entry.getKey();
            Param[] params = entry.getValue();

            Param idParam = params[params.length - 1];
            String id = idParam.getValue()[0].toString();

            // int dbNo = IDGenerator.getDbNo(id);

            List<String> tableCreate = SQLBuildUtils.sql4CreateIndexTable(index);
            String insertIndex = SQLBuildUtils.sql4InsertIndex(index);

            try {
                // Connection connection = dataSourceProxy.getConnection(dbNo);
                Connection connection = dataSourceProxy.getConnection(0);

//                for (String sql : tableCreate) {
//                    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                        preparedStatement.execute();
//                    }
//                }

                try (PreparedStatement preparedStatement = connection.prepareStatement(insertIndex)) {

                    preparedStatement.setString(1, index.getName());

                    for (int i = 0; i < params.length; i++) {
                        Param param = params[i];
                        param.putValue(preparedStatement, i + 2);
                    }

                    preparedStatement.executeUpdate();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @Override
    public ResultSet query(StatNode statNode) {
        try {// TODO deal dbNo.
            Connection connection = dataSourceProxy.getConnection(0);
            final String sql = statNode.toString();

            try (final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                final List<ElementPlaceholderNode> placeholderNodes = statNode.getPlaceholderNodes();
                for (int i = 0, len = placeholderNodes.size(); i < len; i++) {
                    final ElementPlaceholderNode node = placeholderNodes.get(i);
                    node.getParam().putValue(preparedStatement, i + 1);
                }
                return preparedStatement.executeQuery();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(Index index, String id, Object... params) {
        final String sql = SQLBuildUtils.sql4InsertIndex(index);
        final String indexName = index.getName();
        try {
            final Connection connection = this.dataSourceProxy.getConnection(0);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setObject(1, indexName);
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 2, params[i]);
                }
                preparedStatement.setString(2 + params.length, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Index index, String id, Object... params) {
        final String sql = SQLBuildUtils.sql4DeleteIndex(index);
        final String indexName = index.getName();
        try {
            final Connection connection = this.dataSourceProxy.getConnection(0);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                preparedStatement.setObject(2, indexName);
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 3, params[i]);
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int executeUpdate(String sql, Object... params) {
        try {
            final Connection connection = this.dataSourceProxy.getConnection(0);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
