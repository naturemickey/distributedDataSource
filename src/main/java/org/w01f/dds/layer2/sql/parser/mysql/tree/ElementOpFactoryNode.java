package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementOpFactoryNode extends ElementNode implements Cloneable  {

    @Override
    public ElementOpFactoryNode clone() {
        return new ElementOpFactoryNode();
    }
}
