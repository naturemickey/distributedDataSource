package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.sql.PreparedStatement;
import java.util.function.BiConsumer;

public class ElementPlaceholderNode extends ElementTextNode implements Cloneable  {

    public void setSetter(BiConsumer<PreparedStatement, Integer> param) {
        this.setter = setter;
    }

    private BiConsumer<PreparedStatement, Integer> setter;

    public ElementPlaceholderNode(String placeholder) {
        super(placeholder);
        super.placeholderNodes.add(this);
    }

    @Override
    public ElementPlaceholderNode clone() {
        return new ElementPlaceholderNode(super.getTxt());
    }
}
