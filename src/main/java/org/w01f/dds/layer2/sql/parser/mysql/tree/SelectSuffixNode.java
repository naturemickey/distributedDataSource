package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SelectSuffixNode extends SQLSyntaxTreeNode  implements Cloneable {

	private GbobExprsNode orderByExprs;
	private IntPlaceHolderNode offset;
	private IntPlaceHolderNode rowCount;
	private String lock;
	private boolean hasOffsetWord;

	@Override
	public SelectSuffixNode clone() {
		return new SelectSuffixNode(orderByExprs.clone(), offset.clone(), rowCount.clone(), lock, hasOffsetWord);
	}

	public SelectSuffixNode(GbobExprsNode orderByExprs, IntPlaceHolderNode offset, IntPlaceHolderNode rowCount, String lock,
			boolean hasOffsetWord) {
		this.orderByExprs = orderByExprs;
		this.offset = offset;
		this.rowCount = rowCount;
		this.lock = lock;
		this.hasOffsetWord = hasOffsetWord;
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
