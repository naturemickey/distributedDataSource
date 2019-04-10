package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class SelectPrefixNode extends SQLSyntaxTreeNode implements Cloneable {

    private final boolean distinct;
    private final SelectExprsNode selectExprs;
    private final TablesNode tables;
    private final WhereConditionNode where;
    private final GbobExprsNode groupByExprs;
    private final WhereConditionNode having;

    @Override
    public SelectPrefixNode clone() {
        SelectExprsNode selectExprsNode = selectExprs == null ? null : selectExprs.clone();
        TablesNode tablesNode = tables == null ? null : tables.clone();
        WhereConditionNode whereNode = where == null ? null : where.clone();
        GbobExprsNode gbobExprsNode = groupByExprs == null ? null : groupByExprs.clone();
        WhereConditionNode havingNode = having == null ? null : having.clone();
        return new SelectPrefixNode(distinct, selectExprsNode, tablesNode, whereNode, gbobExprsNode, havingNode);
    }

    public SelectPrefixNode(SelectExprsNode selectExprs, TablesNode tables, WhereConditionNode where) {
        this(false, selectExprs, tables, where, null, null);
    }

    public SelectPrefixNode(boolean distinct, SelectExprsNode selectExprs, TablesNode tables, WhereConditionNode where, GbobExprsNode groupByExprs,
                            WhereConditionNode having) {
        this.distinct = distinct;
        this.selectExprs = selectExprs;
        this.tables = tables;
        this.where = where;
        this.groupByExprs = groupByExprs;
        this.having = having;

        setParent(selectExprs, tables, where, groupByExprs, having);
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

    public SelectExprsNode getSelectExprs() {
        return selectExprs;
    }

    public TablesNode getTables() {
        return tables;
    }

    public WhereConditionNode getWhere() {
        return where;
    }

    public GbobExprsNode getGroupByExprs() {
        return groupByExprs;
    }

    public WhereConditionNode getHaving() {
        return having;
    }
}
