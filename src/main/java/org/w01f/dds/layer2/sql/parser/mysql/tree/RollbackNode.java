package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class RollbackNode extends SQLSyntaxTreeNode  implements Cloneable {

	@Override
	public String toString() {
		return "rollback";
	}

	@Override
	public RollbackNode clone() {
		return this;
	}
}
