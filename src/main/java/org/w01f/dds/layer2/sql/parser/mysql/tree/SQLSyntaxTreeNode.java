package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLSyntaxTreeNode implements Cloneable {

    protected List<ElementPlaceholderNode> placeholderNodes = new ArrayList<>();

    public List<ElementPlaceholderNode> getPlaceholderNodes() {
        return placeholderNodes;
    }

    @Override
    public SQLSyntaxTreeNode clone() {
        try {
            return (SQLSyntaxTreeNode) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
