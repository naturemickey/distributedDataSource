package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class UpdateNode extends SQLSyntaxTreeNode implements Cloneable  {

    @Override
    public UpdateNode clone() {
        try {
            return (UpdateNode) super.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
