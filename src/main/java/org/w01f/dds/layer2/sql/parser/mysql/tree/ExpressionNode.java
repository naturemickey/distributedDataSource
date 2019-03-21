package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionNode extends SQLSyntaxTreeNode implements Cloneable  {

    @Override
    public  ExpressionNode clone() {
        return new ExpressionNode();
    }
}
