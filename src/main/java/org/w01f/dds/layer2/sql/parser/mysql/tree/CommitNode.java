package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class CommitNode extends SQLSyntaxTreeNode  implements Cloneable {

	@Override
	public String toString() {
		return "commit";
	}

	@Override
	public CommitNode clone() {
		return this;
	}

}
