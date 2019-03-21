package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementTextParamNode extends ElementTextNode implements Cloneable  {

	public ElementTextParamNode(String txt) {
		super(txt);
	}

	@Override
	public ElementTextParamNode clone() {
		return new ElementTextParamNode(super.getTxt());
	}

}
