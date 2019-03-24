package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementCaseNode extends ElementOpFactoryNode implements Cloneable  {
	private ElementNode value;
	private CaseWhenPartNode caseWhenPart;
	private ElementNode elseEl;

	@Override
	public ElementCaseNode clone() {
		ElementNode valueEN = value == null ? null : value.clone();
		CaseWhenPartNode caseWhenPartNode = caseWhenPart == null ? null : caseWhenPart.clone();
		ElementNode elseEN = elseEl == null ? null : elseEl.clone();
		return new ElementCaseNode(valueEN, caseWhenPartNode, elseEN);
	}

	public ElementCaseNode(ElementNode value, CaseWhenPartNode caseWhenPart, ElementNode elseEl) {
		this.value = value;
		this.caseWhenPart = caseWhenPart;
		this.elseEl = elseEl;

		super.setParent(value, caseWhenPart, elseEl);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("case ");
		if (value != null) {
			sb.append(value).append(' ');
		}
		sb.append(caseWhenPart).append(' ');
		if (elseEl != null) {
			sb.append("else").append(' ').append(elseEl).append(' ');
		}
		sb.append("end");
		return sb.toString();
	}
}