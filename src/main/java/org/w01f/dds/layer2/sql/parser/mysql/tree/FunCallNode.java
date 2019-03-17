package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class FunCallNode extends ElementOpFactoryNode {

	private String name;
	private ParamListNode paramList;

	public FunCallNode(String name, ParamListNode paramList) {
		this.name = name;
		this.paramList = paramList;
	}

	@Override
	public String toString() {
		return name + '(' + paramList + ')';
	}

}
