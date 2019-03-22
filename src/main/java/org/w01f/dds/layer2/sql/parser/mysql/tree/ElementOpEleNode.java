package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementOpEleNode extends ElementNode implements Cloneable  {

	private ElementOpFactoryNode factory;
	private ElementOpEleSuffixNode suffix;

	@Override
	public ElementOpEleNode clone() {
		ElementOpFactoryNode elementOpFactoryNode = factory == null ? null : factory.clone();
		ElementOpEleSuffixNode elementOpEleSuffixNode = suffix == null ? null : suffix.clone();
		return new ElementOpEleNode(elementOpFactoryNode, elementOpEleSuffixNode);
	}

	public ElementOpEleNode(ElementOpFactoryNode factory, ElementOpEleSuffixNode suffix) {
		this.factory = factory;
		this.suffix = suffix;
	}

	@Override
	public String toString() {
		if (suffix != null) {
			return factory.toString() + ' ' + suffix;
		} else {
			return factory.toString();
		}
	}

	public ElementOpFactoryNode getFactory() {
		return factory;
	}

	public void setFactory(ElementOpFactoryNode factory) {
		this.factory = factory;
	}

	public ElementOpEleSuffixNode getSuffix() {
		return suffix;
	}

	public void setSuffix(ElementOpEleSuffixNode suffix) {
		this.suffix = suffix;
	}

}
