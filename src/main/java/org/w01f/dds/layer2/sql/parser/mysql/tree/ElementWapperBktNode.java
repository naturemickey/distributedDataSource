package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementWapperBktNode extends ElementOpFactoryNode implements Cloneable {

    private final ElementNode element;

    @Override
    public ElementWapperBktNode clone() {
        return new ElementWapperBktNode(element == null ? null : element.clone());
    }

    public ElementWapperBktNode(ElementNode element) {
        this.element = element;

        super.setParent(element);
    }

    @Override
    public String toString() {
        return "(" + element + ")";
    }

}
