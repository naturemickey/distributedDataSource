package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementRowNode extends ElementOpFactoryNode  implements Cloneable {

	private ParamListNode paramList;

	@Override
	public ElementRowNode clone() {
		return new ElementRowNode(paramList.clone());
	}

	public ElementRowNode(ParamListNode paramList) {
		this.paramList = paramList;
	}

	@Override
	public String toString() {
		return "row(" + paramList + ")";
	}

}
