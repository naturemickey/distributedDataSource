package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionInValuesNode extends ExpressionNode implements Cloneable  {

	private ElementNode element;
	private boolean not;
	private ValueListNode values;

	@Override
	public ExpressionInValuesNode clone() {
		return new ExpressionInValuesNode(element.clone(), not, values.clone());
	}

	public ExpressionInValuesNode(ElementNode element, boolean not, ValueListNode values) {
		this.element = element;
		this.not = not;
		this.values = values;

		setParent(element, values);
	}

	@Override
	public String toString() {
		if (not) {
			return this.element.toString() + " not in (" + this.values + ")";
		} else {
			return this.element.toString() + " in (" + this.values + ")";
		}
	}
}
