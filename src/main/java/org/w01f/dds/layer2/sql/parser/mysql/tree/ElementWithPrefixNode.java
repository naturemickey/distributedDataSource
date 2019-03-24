package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementWithPrefixNode extends ElementOpFactoryNode  implements Cloneable {

	private String prefix;
	private ElementOpFactoryNode elementOpFactory;

	@Override
	public ElementWithPrefixNode clone() {
		return new ElementWithPrefixNode(prefix, elementOpFactory == null ? null : elementOpFactory.clone());
	}

	public ElementWithPrefixNode(String prefix, ElementOpFactoryNode elementOpFactory) {
		this.prefix = prefix.toLowerCase();
		this.elementOpFactory = elementOpFactory;

		super.setParent(elementOpFactory);
	}

	@Override
	public String toString() {
		return prefix + " " + elementOpFactory;
	}

}
