package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementNode extends SQLSyntaxTreeNode implements Cloneable  {

    @Override
    public ElementNode clone() {
        try {
            return (ElementNode) super.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
