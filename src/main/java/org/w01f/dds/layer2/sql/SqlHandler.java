package org.w01f.dds.layer2.sql;

import org.w01f.dds.layer1.dsproxy.param.Param;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.sql.parser.mysql.tree.DeleteNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.InsertNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.StatNode;
import org.w01f.dds.layer2.sql.parser.mysql.tree.UpdateNode;
import org.w01f.dds.layer2.sql.utils.SQLbreakUtil;
import org.w01f.dds.layer3.indexapi.IIndexAccess;
import org.w01f.dds.layer4.index.IndexAccess;

import java.sql.ResultSet;
import java.util.Map;

public class SqlHandler {

    private IIndexAccess indexAccess = new IndexAccess();

    public ResultSet executeQuery(StatNode statNode) {
        return null;
    }

    public int executeUpdate(StatNode statNode) {
        if (statNode.isInsert()) {
            InsertNode insertNode = statNode.getDmlAsInsert();
            Map<Index, Param[]> indexMap = SQLbreakUtil.parseInsertIndex(insertNode);

            this.indexAccess.insert(indexMap);

            // TODO: need add logic for data insert.
        } else if (statNode.isUpdate()) {
            UpdateNode updateNode = statNode.getDmlAsUpdate();
        } else if (statNode.isDelete()) {
            DeleteNode deleteNode = statNode.getDmlAsDelete();
        }

        return 1;
    }

    public boolean execute(StatNode statNode) {
        executeUpdate(statNode);
        return true;
    }
}
