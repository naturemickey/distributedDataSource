package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementWapperBktNode extends ElementOpFactoryNode implements Cloneable  {

	private ElementNode element;

	@Override
	public ElementWapperBktNode clone() {
		return new ElementWapperBktNode(element.clone());
	}

	public ElementWapperBktNode(ElementNode element) {
		this.element = element;
	}

	@Override
	public String toString() {
		return "(" + element + ")";
	}

}
