package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ValueListNode extends SQLSyntaxTreeNode implements Cloneable  {

	private List<ElementNode> elements;

	@Override
	public ValueListNode clone() {
		List<ElementNode> elements = null;
		if (this.elements != null) {
			elements = new ArrayList<>(this.elements.size());
			for (ElementNode element : this.elements) {
				elements.add(element.clone());
			}
		}
		return new ValueListNode(elements);
	}
	
	public ValueListNode(List<ElementNode> elements) {
		this.elements = elements;

		setParent(elements);
	}

	public List<ElementNode> getElements() {
		return elements;
	}

	@Override
	public String toString() {
		return this.elements.stream().map(e -> e.toString()).collect(Collectors.joining(", "));
	}
	
	public void addElementNode(ElementNode node) {
		this.elements.add(node);
	}

}
