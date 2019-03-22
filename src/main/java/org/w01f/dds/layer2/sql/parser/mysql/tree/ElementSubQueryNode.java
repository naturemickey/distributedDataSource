package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementSubQueryNode extends ElementOpFactoryNode  implements Cloneable {
	private String with;
	private SelectNode select;

	@Override
	public ElementSubQueryNode clone() {
		return new ElementSubQueryNode(with, select == null ? null : select.clone());
	}

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
