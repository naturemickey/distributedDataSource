package org.w01f.dds.layer3.indexapi;

import org.w01f.dds.layer1.dsproxy.param.Param;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.sql.parser.mysql.tree.ExpressionNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IIndexAccess {

    void insert(Map<Index, Param[]> indexMap);

    void insert(Index index, String id, Object... params);

    void delete(Index index, String id, Object... params);

    ResultSet query(Index index, String slotValue, List<ExpressionNode> newIndexWhereNodes);

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

}
