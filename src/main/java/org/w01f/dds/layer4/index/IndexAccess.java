package org.w01f.dds.layer4.index;

import org.w01f.dds.layer1.dsproxy.param.Param;
import org.w01f.dds.layer1.id.IDGenerator;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer3.indexapi.IIndexAccess;
import org.w01f.dds.layer5.DataSourceProxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class IndexAccess implements IIndexAccess {

    private DataSourceProxy dataSourceProxy = DataSourceProxy.getInstance();

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
}
