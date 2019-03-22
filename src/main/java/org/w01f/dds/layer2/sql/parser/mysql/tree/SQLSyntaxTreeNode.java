package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SQLSyntaxTreeNode implements Cloneable {

    @Override
    public SQLSyntaxTreeNode clone() {
        try {
            return (SQLSyntaxTreeNode) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
