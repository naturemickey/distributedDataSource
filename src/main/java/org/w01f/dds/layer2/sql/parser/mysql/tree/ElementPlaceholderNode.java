package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.sql.PreparedStatement;
import java.util.function.BiConsumer;

public class ElementPlaceholderNode extends ElementTextNode implements Cloneable {

    private BiConsumer<PreparedStatement, Integer> setter;

    public void setSetter(BiConsumer<PreparedStatement, Integer> param) {
        this.setter = param;
    }

    public BiConsumer<PreparedStatement, Integer> setter() {
        return setter;
    }

    public ElementPlaceholderNode(String placeholder) {
        super(placeholder);
    }

    @Override
    public ElementPlaceholderNode clone() {
        return new ElementPlaceholderNode(super.getTxt());
    }
}
