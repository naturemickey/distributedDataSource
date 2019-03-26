package org.w01f.dds.layer4.data;

import org.w01f.dds.layer1.id.IDGenerator;
import org.w01f.dds.layer3.dataapi.IDataAccess;
import org.w01f.dds.layer5.DataSourceProxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DataAccess implements IDataAccess {

    private DataSourceProxy dataSourceProxy;

    public void setDataSourceProxy(DataSourceProxy dataSourceProxy) {
        this.dataSourceProxy = dataSourceProxy;
    }

    @Override
    public void insert(String tableName, Map<String, Object> valueMap) {
        String id = (String) valueMap.get("id");
        if (id == null) {
            throw new RuntimeException("there is no id field in the sql.");
        }
        int dbNo = IDGenerator.getDbNo(id);

        Object[] values = new Object[valueMap.size()];
        StringBuilder sqlSb = new StringBuilder("insert into ").append(tableName).append(" (");

        int index = 0;
        for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
            String columnName = entry.getKey();
            Object value = entry.getValue();
            values[index++] = value;

            sqlSb.append(columnName).append(", ");
        }
        sqlSb.delete(sqlSb.length() - 3, sqlSb.length() - 1);
        sqlSb.append(") values (");
        for (int i = 0, len = values.length; i < len; i++) {
            sqlSb.append("?, ");
        }
        sqlSb.delete(sqlSb.length() - 3, sqlSb.length() - 1);
        sqlSb.append(")");

        try {
            Connection connection = dataSourceProxy.getConnection(dbNo);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSb.toString());
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int delete(String tableName, String[] ids) {
        return 0;
    }

    @Override
    public int delete(String tableName, String id) {
        return 0;
    }

    @Override
    public int update(String tableName, Map<String, Object> valueMap, String id) {
        return 0;
    }

    @Override
    public int update(String tableName, Map<String, Object> valueMap, String[] ids) {
        return 0;
    }

    @Override
    public ResultSet select(String tableName, String id) {
        return null;
    }

    @Override
    public ResultSet select(String tableName, String[] ids) {
        return null;
    }
}
