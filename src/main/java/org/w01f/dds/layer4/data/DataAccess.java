package org.w01f.dds.layer4.data;

import org.w01f.dds.layer1.id.IDGenerator;
import org.w01f.dds.layer3.dataapi.IDataAccess;
import org.w01f.dds.layer5.DataSourceProxy;
import org.w01f.dds.layer5.ResultSetProxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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


    private Map<String, String> deletePrefixCache = new ConcurrentHashMap<>();

    private String buildDeletePrefix(String tableName) {
        return "delete from " + tableName + " where id in(";
    }

    private String getDeletePrefix(String tableName) {
        return deletePrefixCache.computeIfAbsent(tableName, this::buildDeletePrefix);
    }

    @Override
    public int delete(String tableName, String[] ids) {
        Map<Integer, List<String>> dbIdsMap = splitIds(ids);

        // return dbIdsMap.entrySet().stream().map(e -> this.delete(sqlPrefix, e.getKey(), e.getValue())).reduce(0, Integer::sum);

        int res = 0;
        for (Map.Entry<Integer, List<String>> entry : dbIdsMap.entrySet()) {
            Integer dbNo = entry.getKey();
            List<String> idList = entry.getValue();
            res += this.delete(tableName, dbNo, idList);
        }
        return res;
    }

    private Map<Integer, List<String>> splitIds(String[] ids) {
        Map<Integer, List<String>> dbIdsMap = new HashMap<>();
        for (String id : ids) {
            int dbNo = IDGenerator.getDbNo(id);
            List<String> idList = dbIdsMap.getOrDefault(dbNo, new ArrayList<>());
            idList.add(id);
        }
        return dbIdsMap;
    }

    private int delete(String tableName, Integer dbNo, List<String> ids) {
        String sqlPrefix = getDeletePrefix(tableName);
        StringBuilder sqlSb = new StringBuilder(sqlPrefix);
        for (int i = 0, len = ids.size(); i < len; i++) {
            sqlSb.append("?, ");
        }
        sqlSb.delete(sqlSb.length() - 3, sqlSb.length() - 1);
        sqlSb.append(")");

        String sql = sqlSb.toString();

        try {
            Connection connection = dataSourceProxy.getConnection(dbNo);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < ids.size(); i++) {
                String id = ids.get(i);
                preparedStatement.setObject(i + 1, id);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(String tableName, String id) {
        String sql = new StringBuilder("delete from ").append(tableName).append(" where id = ?").toString();

        try {
            Connection connection = this.dataSourceProxy.getConnection(IDGenerator.getDbNo(id));
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(String tableName, Map<String, Object> valueMap, String id) {

        Object[] values = new Object[valueMap.size()];
        StringBuilder sqlSb = new StringBuilder("update ").append(tableName).append(" set ");

        int idx = 0;
        for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
            String columnName = entry.getKey();
            Object value = entry.getValue();
            sqlSb.append(columnName).append(" = ?, ");
            values[idx++] = value;
        }
        sqlSb.delete(sqlSb.length() - 3, sqlSb.length() - 1);
        sqlSb.append(" where id = ?");

        try {
            Connection connection = dataSourceProxy.getConnection(IDGenerator.getDbNo(id));
            PreparedStatement preparedStatement = connection.prepareStatement(sqlSb.toString());
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            preparedStatement.setString(values.length, id);

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(String tableName, Map<String, Object> valueMap, String[] ids) {
        // TODO : improve this method in furture.
        int sum = 0;

        for (String id : ids) {
            sum += this.update(tableName, valueMap, id);
        }

        return sum;
    }

    @Override
    public ResultSet select(String tableName, String id) {
        return this.select(tableName, null, id);
    }

    @Override
    public ResultSet select(String tableName, String[] ids) {
        return this.select(tableName, null, ids);
    }

    private Supplier<ResultSet> select(String tableName, String[] columns, Integer dbNo, List<String> ids) {
        String columnNames;
        if (columns == null)
            columnNames = "*";
        else
            columnNames = Arrays.asList(columns).stream().collect(Collectors.joining(", "));
        String prefix = "select " + columnNames + " from " + tableName + " where id in (";
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < ids.size(); i++) {
            sb.append("?, ");
        }
        sb.delete(sb.length() - 3, sb.length() - 1);
        sb.append(")");

        Supplier<ResultSet> supplier = () -> {
            try {
                Connection connection = this.dataSourceProxy.getConnection(dbNo);
                PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());

                for (int i = 0; i < ids.size(); i++) {
                    String id = ids.get(i);
                    preparedStatement.setString(i + 1, id);
                }
                return preparedStatement.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
        return supplier;
    }

    @Override
    public ResultSet select(String tableName, String[] columns, String id) {
        String columnNames;
        if (columns == null)
            columnNames = "*";
        else
            columnNames = Arrays.asList(columns).stream().collect(Collectors.joining(", "));
        String sql = "select " + columnNames + " from " + tableName + " where id = ?";

        try {
            Connection connection = dataSourceProxy.getConnection(IDGenerator.getDbNo(id));
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResultSet select(String tableName, String[] columns, String[] ids) {
        Map<Integer, List<String>> dbIdsMap = splitIds(ids);

        List<Supplier<ResultSet>> supplierList = new ArrayList<>(dbIdsMap.size());
        for (Map.Entry<Integer, List<String>> entry : dbIdsMap.entrySet()) {
            Integer dbNo = entry.getKey();
            List<String> idList = entry.getValue();

            supplierList.add(this.select(tableName, columns, dbNo, idList));
        }
        return new ResultSetProxy(supplierList).getProxy();
    }
}
