package org.w01f.dds.layer3.indexapi;

import org.w01f.dds.layer1.dsproxy.param.Param;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.sql.parser.mysql.tree.ExpressionNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.StatNode;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface IIndexAccess {


//    void addUnique(String indexName, String value, String id);
//    void addNonunique(String indexName, String value, String id);
//    void deleteUnique(String indexName, String value);
//    void deleteNonUnique(String indexName, String value);
//    String queryUnique(String indexName, String value);
//    List<String> queryNonunique(String indexName, String value);

    void insert(Map<Index, Param[]> indexMap);

    void insert(Index index, String id, Object... params);

    void delete(Index index, String id, Object... params);

    ResultSet query(Index index, List<ExpressionNode> newIndexWhereNodes);


}
