package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionInSelectNode extends ExpressionNode implements Cloneable  {

	private ElementNode element;
	private boolean not;
	private SelectNode select;

	@Override
	public ExpressionInSelectNode clone() {
		return new ExpressionInSelectNode(element.clone(), not, select.clone());
	}
	public ExpressionInSelectNode(ElementNode element, boolean not, SelectNode select) {
		this.element = element;
		this.not = not;
		this.select = select;
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
