package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementOpEleSuffixNode extends ElementNode implements Cloneable {

    private final String op;
    private final ElementOpEleNode element;

    @Override
    public ElementOpEleSuffixNode clone() {
        return new ElementOpEleSuffixNode(op, element == null ? null : element.clone());
    }

    public ElementOpEleSuffixNode(String op, ElementOpEleNode element) {
        this.op = op;
        this.element = element;

        super.setParent(element);
    }

    @Override
    public String toString() {
        return op + ' ' + element;
    }

}
