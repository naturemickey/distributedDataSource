package org.w01f.dds.layer2.sql;

import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.sql.parser.mysql.ParserUtils;
import org.w01f.dds.layer2.sql.parser.mysql.tree.InsertNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.StatNode;
import org.w01f.dds.layer3.indexapi.IIndexAccess;
import org.w01f.dds.layer4.index.IndexAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.function.BiConsumer;

public class SqlHandler {

    private IIndexAccess indexAccess = new IndexAccess();

    public ResultSet executeQuery(StatNode statNode) {
        return null;
    }

    public int executeUpdate(StatNode statNode) {
        if (statNode.isInsert()) {
            InsertNode insertNode = statNode.getDmlAsInsert();
            Map<Index, BiConsumer<PreparedStatement, Integer>[]> indexMap = ParserUtils.parseInsertIndex(insertNode);
        }
        return 0;
    }

    public boolean execute(StatNode statNode) {
        return false;
    }
}
