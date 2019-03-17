package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementListFactorNode extends ElementNode {
	private ElementListNode elementList;

	public ElementListFactorNode(ElementListNode elementList) {
		this.elementList = elementList;
	}

	@Override
	public String toString() {
		return "(" + elementList + ")";
	}
}
