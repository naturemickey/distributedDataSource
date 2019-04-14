package org.w01f.dds.layer4.index;

import org.w01f.dds.layer1.dsproxy.param.Param;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.sql.parser.mysql.tree.ElementPlaceholderNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.ExpressionNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.StatNode;
import org.w01f.dds.layer2.sql.utils.SQLBuildUtils;
import org.w01f.dds.layer3.indexapi.IIndexAccess;
import org.w01f.dds.layer5.DataSourceProxy;
import org.w01f.dds.utils.SlotConsistentHashing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexAccess implements IIndexAccess {

    private DataSourceProxy dataSourceProxy;
    private Map<Integer, Integer> slotDsMap = new HashMap<>();

    {
        for (int i = 0; i < SlotConsistentHashing.getSlotcount(); i++) {
            slotDsMap.put(i, 0);
        }
    }

    public void setDataSourceProxy(DataSourceProxy dataSourceProxy) {
        this.dataSourceProxy = dataSourceProxy;
    }

    public void setSlotDsMap(Map<Integer, Integer> slotDsMap) {
        this.slotDsMap = slotDsMap;
    }

    private int getSlotNo(String key) {
        return SlotConsistentHashing.getSlotNo(key);
    }

    private int getDbNo(String key) {
        final int slotNo = SlotConsistentHashing.getSlotNo(key);
        return slotDsMap.get(slotNo);
    }

    @Override
    public void insert(Map<Index, Param[]> indexMap) {
        for (Map.Entry<Index, Param[]> entry : indexMap.entrySet()) {
            Index index = entry.getKey();
            Param[] params = entry.getValue();

            insert(index, params);
        }
    }

    private void insert(Index index, Param[] params) {
        final String slotValue = params[0].getValue()[1].toString();
        final int dbNo = getDbNo(slotValue);
        final int slotNo = getSlotNo(slotValue);

        final String insertSQL = SQLBuildUtils.sql4InsertIndex(index, slotNo);

        try {
            Connection connection = dataSourceProxy.getConnection(dbNo);

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, index.getName());
                for (int i = 0; i < params.length; i++) {
                    params[i].putValue(preparedStatement, i + 2);
                }
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet query(Index index, String slotValue, List<ExpressionNode> newIndexWhereNodes) {
        final int dbNo = getDbNo(slotValue);
        final StatNode statNode = SQLBuildUtils.sql4QueryIndex(index, newIndexWhereNodes, getSlotNo(slotValue));
        try {
            Connection connection = dataSourceProxy.getConnection(dbNo);
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
        final String slotValue = params[0].toString();
        final int dbNo = getDbNo(slotValue);

        final String sql = SQLBuildUtils.sql4InsertIndex(index, getSlotNo(slotValue));
        final String indexName = index.getName();

        try {
            final Connection connection = this.dataSourceProxy.getConnection(dbNo);

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
        final String slotValue = params[0].toString();
        final int dbNo = getDbNo(slotValue);

        final String sql = SQLBuildUtils.sql4DeleteIndex(index, getSlotNo(slotValue));
        final String indexName = index.getName();

        try {
            final Connection connection = this.dataSourceProxy.getConnection(dbNo);

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

    @Override
    public void commit() throws SQLException {
        this.dataSourceProxy.commitAll();
    }

    @Override
    public void rollback() throws SQLException {
        this.dataSourceProxy.rollbackAll();
    }

    @Override
    public void close() throws SQLException {

    }
}
