package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class GbobExprsNode extends SQLSyntaxTreeNode implements Cloneable {
    private final ElementNode element;
    private final String sc;
    private final GbobExprsNode suffix;

    @Override
    public GbobExprsNode clone() {
        ElementNode elementNode = element == null ? null : element.clone();
        GbobExprsNode gbobExprsNode = suffix == null ? null : suffix.clone();
        return new GbobExprsNode(elementNode, sc, gbobExprsNode);
    }

    public GbobExprsNode(ElementNode element, String sc, GbobExprsNode suffix) {
        this.element = element;
        this.sc = (sc == null) ? null : sc.toLowerCase();
        this.suffix = suffix;

        setParent(element, suffix);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(element);
        if (sc != null)
            sb.append(' ').append(sc);
        if (suffix != null)
            sb.append(", ").append(suffix);
        return sb.toString();
    }

}
