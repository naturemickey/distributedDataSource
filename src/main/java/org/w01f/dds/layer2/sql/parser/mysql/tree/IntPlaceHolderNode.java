package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class IntPlaceHolderNode extends SQLSyntaxTreeNode implements Cloneable {

    private final String intText;
    private final ElementPlaceholderNode placeholderNode;

    @Override
    public IntPlaceHolderNode clone() {
        if (intText != null) return new IntPlaceHolderNode(intText);
        return new IntPlaceHolderNode(placeholderNode == null ? null : placeholderNode.clone());
    }

    public IntPlaceHolderNode(ElementPlaceholderNode placeholderNode) {
        this.placeholderNode = placeholderNode;
        this.intText = null;

        setParent(placeholderNodes);
    }

    public IntPlaceHolderNode(String intText) {
        this.intText = intText;
        this.placeholderNode = null;
    }

    @Override
    public String toString() {
        return intText != null ? intText : placeholderNode.toString();
    }
}
