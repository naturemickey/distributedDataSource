package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementWithPrefixNode extends ElementOpFactoryNode {

	private String prefix;
	private ElementOpFactoryNode elementOpFactory;

	public ElementWithPrefixNode(String prefix, ElementOpFactoryNode elementOpFactory) {
		this.prefix = prefix.toLowerCase();
		this.elementOpFactory = elementOpFactory;
	}

	@Override
	public String toString() {
		return prefix + " " + elementOpFactory;
	}

}
