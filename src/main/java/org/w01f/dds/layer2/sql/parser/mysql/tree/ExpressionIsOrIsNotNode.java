package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ExpressionIsOrIsNotNode extends ExpressionNode  implements Cloneable {

	private ElementNode element;
	private boolean not;
	private String what;

	@Override
	public ExpressionIsOrIsNotNode clone() {
		return new ExpressionIsOrIsNotNode(element == null ? null : element.clone(), not, what);
	}

	public ExpressionIsOrIsNotNode(ElementNode element, boolean not, String what) {
		this.element = element;
		this.not = not;
		this.what = what.toLowerCase();
	}

	@Override
	public String toString() {
		if (!not) {
			return this.element + " is " + what;
		} else {
			return this.element + " is not " + what;
		}
	}

}
