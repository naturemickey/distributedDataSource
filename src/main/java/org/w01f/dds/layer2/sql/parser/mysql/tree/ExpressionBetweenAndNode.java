package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionBetweenAndNode extends ExpressionNode implements Cloneable  {
	private ElementNode element;
	private boolean not;
	private ElementNode left;
	private ElementNode right;

	@Override
	public ExpressionBetweenAndNode clone() {
		return new ExpressionBetweenAndNode(element.clone(), not, left.clone(), right.clone());
	}

	public ExpressionBetweenAndNode(ElementNode element, boolean not, ElementNode left, ElementNode right) {
		this.element = element;
		this.not = not;
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return element.toString() + (not ? " not " : "") + " between " + left + " and " + right;
	}
}
