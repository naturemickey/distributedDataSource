package org.w01f.dds.layer2.sql.parser.mysql.tree;

public final class ElementDateNode extends ElementOpFactoryNode  implements Cloneable {
	private final String dt;
	private final String str;

	@Override
	public ElementDateNode clone() {
		return new ElementDateNode(dt, str);
	}

	public ElementDateNode(String dt, String str) {
		this.dt = dt.toLowerCase();
		this.str = str;
	}

	@Override
	public String toString() {
		return dt + ' ' + str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dt == null) ? 0 : dt.hashCode());
		result = prime * result + ((str == null) ? 0 : str.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementDateNode other = (ElementDateNode) obj;
		if (dt == null) {
			if (other.dt != null)
				return false;
		} else if (!dt.equals(other.dt))
			return false;
		if (str == null) {
			if (other.str != null)
				return false;
		} else if (!str.equals(other.str))
			return false;
		return true;
	}
}
