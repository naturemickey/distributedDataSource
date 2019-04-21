package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class StatNode extends SQLSyntaxTreeNode {

    public SQLSyntaxTreeNode getDml() {
        return dml;
    }

    private final SQLSyntaxTreeNode dml;

    public StatNode(SQLSyntaxTreeNode dml) {
        this.dml = dml;

        setParent(dml);
    }

    @Override
    public String toString() {
        return dml.toString();
    }

    public boolean isInsert() {
        return dml instanceof InsertNode;
    }

    public boolean isUpdate() {
        return dml instanceof UpdateNode;
    }

    public boolean isDelete() {
        return dml instanceof DeleteNode;
    }

    public boolean isSelect() {
        return dml instanceof SelectNode;
    }

    public InsertNode getDmlAsInsert() {
        return (InsertNode) dml;
    }

    public UpdateNode getDmlAsUpdate() {
        return ((UpdateNode) dml);
    }

    public DeleteNode getDmlAsDelete() {
        return DeleteNode.class.cast(dml);
    }

    public SelectNode getDmlAsSelect() {
        return (SelectNode) dml;
    }
}
