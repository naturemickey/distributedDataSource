package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SelectNode extends SQLSyntaxTreeNode implements Cloneable {
	private final SelectInner selectInner;
	private final SelectUnionSuffix suffix;

	@Override
	public SelectNode clone() {
		SelectInner selectInner2 = selectInner == null ? null : selectInner.clone();
		SelectUnionSuffix selectUnionSuffix = suffix == null ? null : suffix.clone();
		return new SelectNode(selectInner2, selectUnionSuffix);
	}

	public SelectNode(SelectInner selectInner) {
		this(selectInner, null);
	}

	public SelectNode(SelectInner selectInner, SelectUnionSuffix suffix) {
		this.selectInner = selectInner;
		this.suffix = suffix;

		super.setParent(selectInner, suffix);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (suffix != null) {
			sb.append('(').append(selectInner).append(')').append(' ').append(suffix);
		} else {
			sb.append(selectInner);
		}
		return sb.toString();
	}

	public SelectInner getSelectInner() {
		return selectInner;
	}

	public SelectUnionSuffix getSuffix() {
		return suffix;
	}
}
