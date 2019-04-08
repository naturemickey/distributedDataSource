package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SelectElementNode extends SQLSyntaxTreeNode {
    private final ElementNode element;
    private final String alias;


    public SelectElementNode(ElementNode element) {
        this.element = element;
        this.alias = null;

        super.setParent(element);
    }

    public SelectElementNode(ElementNode element, String alias) {
        this.element = element;
        this.alias = alias;

        super.setParent(element);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(element);
        if (alias != null)
            sb.append(" as ").append(alias);
        return sb.toString();
    }

    @Override
    public SelectElementNode clone() {
        return new SelectElementNode(element == null ? null : element.clone(), alias);
    }
}
