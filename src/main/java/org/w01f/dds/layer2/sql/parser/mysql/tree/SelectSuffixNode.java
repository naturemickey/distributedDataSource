package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SelectSuffixNode extends SQLSyntaxTreeNode  implements Cloneable {

	private final GbobExprsNode orderByExprs;
	private final IntPlaceHolderNode offset;
	private final IntPlaceHolderNode rowCount;
	private final String lock;
	private final boolean hasOffsetWord;

	@Override
	public SelectSuffixNode clone() {
		GbobExprsNode gbobExprsNode = orderByExprs == null ? null :orderByExprs.clone();
		IntPlaceHolderNode offsetNode = offset == null ? null : offset.clone();
		IntPlaceHolderNode rowCountNode = rowCount == null ? null : rowCount.clone();
		return new SelectSuffixNode(gbobExprsNode, offsetNode, rowCountNode, lock, hasOffsetWord);
	}

	public SelectSuffixNode(GbobExprsNode orderByExprs, IntPlaceHolderNode offset, IntPlaceHolderNode rowCount, String lock,
			boolean hasOffsetWord) {
		this.orderByExprs = orderByExprs;
		this.offset = offset;
		this.rowCount = rowCount;
		this.lock = lock;
		this.hasOffsetWord = hasOffsetWord;

		setParent(orderByExprs, offset, rowCount);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.orderByExprs != null)
			sb.append(" order by ").append(this.orderByExprs);
		if (this.rowCount != null) {
			sb.append(" limit ");
			if (this.hasOffsetWord) {
				sb.append(this.rowCount).append(" offset ").append(this.offset);
			} else {
				if (this.offset != null)
					sb.append(this.offset).append(", ");
				sb.append(this.rowCount);
			}
		}
		if (this.lock != null) {
			sb.append(' ').append(this.lock);
		}

		return sb.toString();
	}

}
