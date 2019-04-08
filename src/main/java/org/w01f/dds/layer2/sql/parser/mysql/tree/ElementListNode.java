package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ElementListNode extends SQLSyntaxTreeNode implements Cloneable  {

	private final List<ElementNode> elements;

	@Override
	public ElementListNode clone() {
		List<ElementNode> elements = null;
		if (this.elements != null) {
			elements = new ArrayList<>(this.elements.size());
			for (ElementNode element : this.elements) {
				elements.add(element.clone());
			}
		}
		return new ElementListNode(elements);
	}

	public ElementListNode(List<ElementNode> elements) {
		this.elements = Collections.unmodifiableList(elements);

		super.setParent(elements);
	}

	@Override
	public String toString() {
		return elements.stream().map(n -> n.toString()).collect(Collectors.joining(", "));
	}

}
