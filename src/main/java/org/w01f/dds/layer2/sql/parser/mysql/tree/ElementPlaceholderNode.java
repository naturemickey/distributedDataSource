package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementPlaceholderNode extends ElementTextNode implements Cloneable  {

    public void setParam(Object param) {
        this.param = param;
    }

    private Object param;

    public ElementPlaceholderNode(String placeholder) {
        super(placeholder);
    }

    @Override
    public ElementPlaceholderNode clone() {
        return new ElementPlaceholderNode(super.getTxt());
    }
}
