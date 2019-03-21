package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class IntPlaceHolderNode extends SQLSyntaxTreeNode {

    private String intText;
    private ElementPlaceholderNode placeholderNode;

    public IntPlaceHolderNode(ElementPlaceholderNode placeholderNode) {
        this.placeholderNode = placeholderNode;
    }

    public IntPlaceHolderNode(String intText) {
        this.intText = intText;
    }

    @Override
    public String toString() {
        return intText != null ? intText : placeholderNode.toString();
    }
}
