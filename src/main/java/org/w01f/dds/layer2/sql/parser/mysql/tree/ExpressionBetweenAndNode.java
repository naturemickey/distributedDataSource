package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionBetweenAndNode extends ExpressionNode implements Cloneable  {
	private ElementNode element;
	private boolean not;
	private ElementNode left;
	private ElementNode right;

	@Override
	public ExpressionBetweenAndNode clone() {
		ElementNode elementNode = element == null ? null :element.clone();
		ElementNode leftNode = left == null ? null : left.clone();
		ElementNode rightNode = right == null ? null : right.clone();
		return new ExpressionBetweenAndNode(elementNode, not, leftNode, rightNode);
	}

	public ExpressionBetweenAndNode(ElementNode element, boolean not, ElementNode left, ElementNode right) {
		this.element = element;
		this.not = not;
		this.left = left;
		this.right = right;

		super.setParent(element, left, right);
	}

	@Override
	public String toString() {
		return element.toString() + (not ? " not " : "") + " between " + left + " and " + right;
	}

	public ElementNode getElement() {
		return element;
	}

	public boolean isNot() {
		return not;
	}

	public ElementNode getLeft() {
		return left;
	}

	public ElementNode getRight() {
		return right;
	}
}
