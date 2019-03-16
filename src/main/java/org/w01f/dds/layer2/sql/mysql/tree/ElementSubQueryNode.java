package org.w01f.dds.layer2.sql.mysql.tree;

public class ElementSubQueryNode extends ElementOpFactoryNode {
	private String with;
	private SelectNode select;

	public ElementSubQueryNode(String with, SelectNode select) {
		this.with = with;
		this.select = select;
	}

	@Override
	public String toString() {
		if (with != null) {
			return with + " (" + select + ")";
		} else {
			return "(" + select + ")";
		}
	}
}
