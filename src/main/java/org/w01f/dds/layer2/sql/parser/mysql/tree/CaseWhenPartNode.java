package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class CaseWhenPartNode extends SQLSyntaxTreeNode implements Cloneable {
    private ElementNode whenEl;
    private ExpressionRelationalNode whenRe;
    private ElementNode then;
    private CaseWhenPartNode suffix;

    @Override
    public CaseWhenPartNode clone() {
        ElementNode whenElementNode = whenEl == null ? null : whenEl.clone();
        ExpressionRelationalNode whenRelationalNode = whenRe == null ? null : whenRe.clone();
        ElementNode thenElementNode = then == null ? null : then.clone();
        CaseWhenPartNode suffixCW = suffix == null ? null : suffix.clone();
        return new CaseWhenPartNode(whenElementNode, whenRelationalNode, thenElementNode, suffixCW);
    }

    public CaseWhenPartNode(ElementNode whenEl, ExpressionRelationalNode whenRe, ElementNode then, CaseWhenPartNode suffix) {
        this.whenEl = whenEl;
        this.whenRe = whenRe;
        this.then = then;
        this.suffix = suffix;

        setParent(whenEl, whenRe, then, suffix);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("when ");
        if (whenEl != null) {
            sb.append(whenEl).append(' ');
        }
        if (whenRe != null) {
            sb.append(whenRe).append(' ');
        }
        sb.append("then ").append(then);
        if (suffix != null) {
            sb.append(' ').append(suffix);
        }
        return sb.toString();
    }
}
