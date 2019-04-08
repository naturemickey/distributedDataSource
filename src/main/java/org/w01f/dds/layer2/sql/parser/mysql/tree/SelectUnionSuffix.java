package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SelectUnionSuffix extends SQLSyntaxTreeNode implements Cloneable  {
	private final String method;
	private final SelectNode select;
	// private SelectSuffixNode suffix;

	@Override
	public SelectUnionSuffix clone() {
		SelectNode selectNode = select == null ?null :select.clone();
		// SelectSuffixNode selectSuffixNode = suffix == null ? null :suffix.clone();
		// return new SelectUnionSuffix(method, selectNode, selectSuffixNode);
		return new SelectUnionSuffix(method, selectNode);
	}

	public SelectUnionSuffix(String method, SelectNode select) {
		this.method = method;
		this.select = select;
		// this.suffix = suffix;

		setParent(select);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("union ");
		if (method != null) {
			sb.append(method).append(' ');
		}
		sb.append("(").append(select).append(")");
//		if (suffix != null) {
//			sb.append(' ').append(suffix);
//		}
		return sb.toString();
	}
}
