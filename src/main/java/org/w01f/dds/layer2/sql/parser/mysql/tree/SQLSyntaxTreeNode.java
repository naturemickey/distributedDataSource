package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class SQLSyntaxTreeNode implements Cloneable {

    protected SQLSyntaxTreeNode parent;
    protected List<SQLSyntaxTreeNode> children= new ArrayList<>();

    protected List<ElementPlaceholderNode> placeholderNodes = null;

    public List<ElementPlaceholderNode> getPlaceholderNodes() {
        if (placeholderNodes == null) {
            placeholderNodes = new ArrayList<>();

            for (SQLSyntaxTreeNode node : children) {
                if (node != null) {
                    if (node instanceof ElementPlaceholderNode) {
                        placeholderNodes.add((ElementPlaceholderNode) node);
                    }else {
                        placeholderNodes.addAll(node.getPlaceholderNodes());
                    }
                }
            }
        }
        return placeholderNodes;
    }

    @Override
    public SQLSyntaxTreeNode clone() {
        try {
            return (SQLSyntaxTreeNode) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setParent(SQLSyntaxTreeNode... nodes) {
        for (SQLSyntaxTreeNode node : nodes) {
            if (node != null) {
                node.parent = this;
                this.children.add(node);
            }
        }
    }

    public <T extends SQLSyntaxTreeNode> void setParent(List<T> nodes) {
        if (nodes == null) {
            return;
        }
        for (T node : nodes) {
            if (node != null) {
                node.parent = this;
                this.children.add(node);
            }
        }
    }
}
