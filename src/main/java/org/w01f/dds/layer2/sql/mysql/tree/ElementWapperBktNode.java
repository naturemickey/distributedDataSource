package org.w01f.dds.layer2.sql.mysql.tree;

public class ElementWapperBktNode extends ElementOpFactoryNode {

	private ElementNode element;

	public ElementWapperBktNode(ElementNode element) {
		this.element = element;
	}

	@Override
	public String toString() {
		return "(" + element + ")";
	}

}
