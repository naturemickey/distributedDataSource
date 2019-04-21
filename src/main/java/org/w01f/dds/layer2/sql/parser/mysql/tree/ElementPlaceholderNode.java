package org.w01f.dds.layer2.sql.parser.mysql.tree;

import org.w01f.dds.layer1.dsproxy.param.Param;

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
        if ("?".equals(placeholder) || placeholder.startsWith(":")) {
            // OK
        } else {
            throw new RuntimeException("wrong placeholder.");
        }
    }

    public static ElementPlaceholderNode createByStringValue(String strVal) {
        final ElementPlaceholderNode elementPlaceholderNode = new ElementPlaceholderNode("?");

        try {
            elementPlaceholderNode.setParam(new Param("setString", new Object[]{1, strVal}));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return elementPlaceholderNode;
    }

    @Override
    public ElementPlaceholderNode clone() {
        return new ElementPlaceholderNode(super.getTxt());
    }
}
