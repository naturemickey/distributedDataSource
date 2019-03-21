package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ColumnNamesNode extends SQLSyntaxTreeNode  implements Cloneable {

	private List<String> names = new ArrayList<>();

	@Override
	public ColumnNamesNode clone() {
		return this;
	}

	public ColumnNamesNode(List<String> names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return names.stream().collect(Collectors.joining(", "));
	}

	public void addColumn(String name) {
		this.names.add(name);
	}
}
