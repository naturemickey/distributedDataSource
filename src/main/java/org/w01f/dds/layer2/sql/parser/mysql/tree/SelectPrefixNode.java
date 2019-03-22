package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SelectPrefixNode extends SQLSyntaxTreeNode implements Cloneable {

    private boolean distinct;
    private SelectExprsNode selectExprs;
    private TablesNode tables;
    private WhereConditionNode where;
    private GbobExprsNode groupByExprs;
    private WhereConditionNode having;

    @Override
    public SelectPrefixNode clone() {
        SelectExprsNode selectExprsNode = selectExprs == null ? null : selectExprs.clone();
        TablesNode tablesNode = tables == null ? null : tables.clone();
        WhereConditionNode whereNode = where == null ? null : where.clone();
        GbobExprsNode gbobExprsNode = groupByExprs == null ? null : groupByExprs.clone();
        WhereConditionNode havingNode = having == null ? null : having.clone();
        return new SelectPrefixNode(distinct, selectExprsNode, tablesNode, whereNode, gbobExprsNode, havingNode);
    }

    public SelectPrefixNode(boolean distinct, SelectExprsNode selectExprs, TablesNode tables, WhereConditionNode where, GbobExprsNode groupByExprs,
                            WhereConditionNode having) {
        this.distinct = distinct;
        this.selectExprs = selectExprs;
        this.tables = tables;
        this.where = where;
        this.groupByExprs = groupByExprs;
        this.having = having;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if (this.distinct)
            sb.append("distinct ");
        sb.append(this.selectExprs.toString());
        if (this.tables != null)
            sb.append(" from ").append(this.tables);
        if (this.where != null)
            sb.append(" where ").append(this.where);
        if (this.groupByExprs != null)
            sb.append(" group by ").append(this.groupByExprs);
        if (this.having != null)
            sb.append(" having ").append(this.having);

        return sb.toString();
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public SelectExprsNode getSelectExprs() {
        return selectExprs;
    }

    public void setSelectExprs(SelectExprsNode selectExprs) {
        this.selectExprs = selectExprs;
    }

    public TablesNode getTables() {
        return tables;
    }

    public void setTables(TablesNode tables) {
        this.tables = tables;
    }

    public WhereConditionNode getWhere() {
        return where;
    }

    public void setWhere(WhereConditionNode where) {
        this.where = where;
    }

    public GbobExprsNode getGroupByExprs() {
        return groupByExprs;
    }

    public void setGroupByExprs(GbobExprsNode groupByExprs) {
        this.groupByExprs = groupByExprs;
    }

    public WhereConditionNode getHaving() {
        return having;
    }

    public void setHaving(WhereConditionNode having) {
        this.having = having;
    }

}
