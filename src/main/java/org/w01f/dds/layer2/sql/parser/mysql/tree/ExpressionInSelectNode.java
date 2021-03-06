package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionInSelectNode extends ExpressionNode implements Cloneable  {

	private final ElementNode element;
	private final boolean not;
	private final SelectNode select;

	@Override
	public ExpressionInSelectNode clone() {
		ElementNode elementNode = element == null ? null : element.clone();
		SelectNode selectNode = select == null ? null : select.clone();
		return new ExpressionInSelectNode(elementNode, not, selectNode);
	}
	public ExpressionInSelectNode(ElementNode element, boolean not, SelectNode select) {
		this.element = element;
		this.not = not;
		this.select = select;

		setParent(element, select);
	}

	@Override
	public String toString() {
		if (not) {
			return this.element.toString() + " not in (" + this.select + ")";
		} else {
			return this.element.toString() + " in (" + this.select + ")";
		}
	}

}
