package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SelectInner extends SQLSyntaxTreeNode implements Cloneable {

	private final SelectPrefixNode prefix;
	private final SelectSuffixNode suffix;

	@Override public SelectInner clone() {
		SelectPrefixNode selectPrefixNode = prefix == null ? null : prefix.clone();
		SelectSuffixNode selectSuffixNode = suffix == null ? null : suffix.clone();
		return new SelectInner(selectPrefixNode, selectSuffixNode);
	}

	public SelectInner(SelectPrefixNode prefix, SelectSuffixNode suffix) {
		this.prefix = prefix;
		this.suffix = suffix;

		setParent(prefix, suffix);
	}

	@Override
	public String toString() {
		String suffixStr = suffix == null ? "" : suffix.toString();
		if (suffixStr.length() > 0)
			return prefix.toString() + suffixStr;
		return prefix.toString();
	}

	public SelectPrefixNode getPrefix() {
		return prefix;
	}

	public SelectSuffixNode getSuffix() {
		return suffix;
	}
}
