package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SelectExprsNode extends SQLSyntaxTreeNode  implements Cloneable {
	private ElementNode element;
	private String alias;
	private SelectExprsNode suffix;

	@Override
	public SelectExprsNode clone() {
		ElementNode elementNode = element == null ? null :element.clone();
		SelectExprsNode exprsNode = suffix == null ? null : suffix.clone();
		return new SelectExprsNode(elementNode, alias, exprsNode);
	}

	public SelectExprsNode(ElementNode element, String alias, SelectExprsNode suffix) {
		this.element = element;
		this.alias = alias;
		this.suffix = suffix;

		setParent(element, suffix);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(element);
		if (alias != null)
			sb.append(" as ").append(alias);
		if (suffix != null)
			sb.append(", ").append(suffix);

		return sb.toString();
	}

	public SelectExprsNode getLastNode() {
		return suffix == null ? this : suffix.getLastNode();
	}

	public SelectExprsNode getSuffix() {
		return suffix;
	}

	public void setSuffix(SelectExprsNode suffix) {
		this.suffix = suffix;
	}

}
