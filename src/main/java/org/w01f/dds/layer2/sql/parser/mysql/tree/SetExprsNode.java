package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetExprsNode extends SQLSyntaxTreeNode  implements Cloneable {

	private List<SetExprNode> setExprs;

	@Override
	public SetExprsNode clone() {
		List<SetExprNode> setExprs = null;
		if (this.setExprs != null) {
			setExprs = new ArrayList<>(this.setExprs.size());
			for (SetExprNode setExpr : this.setExprs) {
				setExprs.add(setExpr.clone());
			}
		}
		return new SetExprsNode(setExprs);
	}

	public SetExprsNode(List<SetExprNode> setExprs) {
		this.setExprs = setExprs;
	}

	@Override
	public String toString() {
		return setExprs.stream().map(se -> se.toString()).collect(Collectors.joining(", "));
	}

	public void addSetExpr(SetExprNode se) {
		this.setExprs.add(se);
	}

}
