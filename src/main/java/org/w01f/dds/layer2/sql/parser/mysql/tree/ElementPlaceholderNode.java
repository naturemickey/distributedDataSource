package org.w01f.dds.layer2.sql.parser.mysql.tree;

import org.w01f.dds.layer1.dsproxy.param.Param;

import java.sql.PreparedStatement;
import java.util.function.BiConsumer;

public class ElementPlaceholderNode extends ElementTextNode implements Cloneable {

    private Param param;

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public ElementPlaceholderNode(String placeholder) {
        super(placeholder);
    }

    @Override
    public ElementPlaceholderNode clone() {
        return new ElementPlaceholderNode(super.getTxt());
    }
}
