package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ParamListNode extends SQLSyntaxTreeNode implements Cloneable{

	private ElementNode element;
	ExpressionRelationalNode exprRelational;
	private ParamListNode suffix;

	@Override
	public ParamListNode clone() {
		return new ParamListNode(element.clone(), exprRelational.clone(), suffix.clone());
	}

	public ParamListNode(ElementNode element, ExpressionRelationalNode exprRelational, ParamListNode suffix) {
		this.element = element;
		this.exprRelational = exprRelational;
		this.suffix = suffix;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (element != null) {
			sb.append(element);
		} else if (exprRelational != null) {
			sb.append(exprRelational);
		}
		if (suffix != null) {
			sb.append(", ").append(suffix);
		}
		return sb.toString();
	}

}
