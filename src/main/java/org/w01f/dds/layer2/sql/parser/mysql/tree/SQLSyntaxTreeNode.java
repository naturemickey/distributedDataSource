package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SQLSyntaxTreeNode implements Cloneable {

    public Map<Field, SQLSyntaxTreeNode> childrenNode() {
        Map<Field, SQLSyntaxTreeNode> res = new HashMap<>();
        for (Field field : this.getClass().getDeclaredFields()) {
            if (SQLSyntaxTreeNode.class.isAssignableFrom(field.getType())) {
                try {
                    field.setAccessible(true);
                    SQLSyntaxTreeNode node = (SQLSyntaxTreeNode) field.get(this);
                    res.put(field, node);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return res;
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
