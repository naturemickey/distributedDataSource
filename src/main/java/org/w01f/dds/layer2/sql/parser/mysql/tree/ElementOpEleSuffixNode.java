package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementOpEleSuffixNode extends ElementNode {

	private String op;
	private ElementOpEleNode element;

	public ElementOpEleSuffixNode(String op, ElementOpEleNode element) {
		this.op = op;
		this.element = element;
	}

	@Override
	public String toString() {
		return op + ' ' + element;
	}

}
