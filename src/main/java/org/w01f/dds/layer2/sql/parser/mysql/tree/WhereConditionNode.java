package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class WhereConditionNode extends SQLSyntaxTreeNode implements Cloneable {


    @Override
    public WhereConditionNode clone() {
        return new WhereConditionNode();
    }
}
