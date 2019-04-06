package org.w01f.dds.layer2.sql.utils.dto;

import org.w01f.dds.layer2.sql.parser.mysql.tree.WhereConditionNode;

public class WhereDto {

    private WhereConditionNode newWhere;
    private WhereConditionNode indexWhere;

    public WhereConditionNode getNewWhere() {
        return newWhere;
    }

    public void setNewWhere(WhereConditionNode newWhere) {
        this.newWhere = newWhere;
    }

    public WhereConditionNode getIndexWhere() {
        return indexWhere;
    }

    public void setIndexWhere(WhereConditionNode indexWhere) {
        this.indexWhere = indexWhere;
    }
}
