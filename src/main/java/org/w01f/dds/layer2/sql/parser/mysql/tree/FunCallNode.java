package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class FunCallNode extends ElementOpFactoryNode implements Cloneable  {

	private String name;
	private ParamListNode paramList;

	@Override
	public FunCallNode clone() {
		return new FunCallNode(name, paramList == null ? null : paramList.clone());
	}

	public FunCallNode(String name, ParamListNode paramList) {
		this.name = name;
		this.paramList = paramList;

		setParent(paramList);
	}

	@Override
	public String toString() {
		return name + '(' + paramList + ')';
	}

}
