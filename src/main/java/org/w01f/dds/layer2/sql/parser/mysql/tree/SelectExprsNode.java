package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SelectExprsNode extends SQLSyntaxTreeNode implements Cloneable {
    private final List<SelectElementNode> nodes;

    @Override
    public SelectExprsNode clone() {
        List<SelectElementNode> nodeList = null;
        if (this.nodes != null) {
            nodeList = new ArrayList<>(this.nodes.size());
            for (SelectElementNode node : this.nodes) {
                nodeList.add(node.clone());
            }
        }
        return new SelectExprsNode(nodeList);
    }

    public SelectExprsNode(List<SelectElementNode> nodes) {
        this.nodes = Collections.unmodifiableList(nodes);

        setParent(nodes);
    }

    @Override
    public String toString() {
        return nodes.stream().map(SelectElementNode::toString).collect(Collectors.joining(", "));
    }

}
