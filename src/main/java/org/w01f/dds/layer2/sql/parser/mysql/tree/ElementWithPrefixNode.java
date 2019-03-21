package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementWithPrefixNode extends ElementOpFactoryNode  implements Cloneable {

	private String prefix;
	private ElementOpFactoryNode elementOpFactory;

	@Override
	public ElementWithPrefixNode clone() {
		return new ElementWithPrefixNode(prefix, elementOpFactory.clone());
	}

	public ElementWithPrefixNode(String prefix, ElementOpFactoryNode elementOpFactory) {
		this.prefix = prefix.toLowerCase();
		this.elementOpFactory = elementOpFactory;
	}

	@Override
	public String toString() {
		return prefix + " " + elementOpFactory;
	}

}
