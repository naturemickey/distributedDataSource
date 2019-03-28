package org.w01f.dds.layer4.index;

import org.w01f.dds.layer2.index.config.Column;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;
import org.w01f.dds.layer3.indexapi.IIndexAccess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IndexAccess implements IIndexAccess {

    /*
    data_type:
    string  : s : varchar(1000)
    date    : d : datetime
    long    : l : numeric(20)

    index_type:
    unique index : u_
    inex         : i_

    table_name: index_type + data_type list

    example:
    create table u_sd (
        index_name  varchar(1000),
        v1          varchar(1000),
        v2          datetime
    );
    create unique index idx_u_sd on u_sd(index_name, v1, v2);
     */
    private static List<String> buildSQL4CreateIndexTable(Index index) {

        String tableName = (index.isUnique() ? "u_" : "i_") + Arrays.asList(index.getColumns()).stream().map(c -> c.getType().getSimpleName()).collect(Collectors.joining());

        StringBuilder createSb = new StringBuilder("create table ").append(tableName).append(" (\n");
        createSb.append("    index_name  varchar(1000),\n");
        for (int i = 0, len = index.getColumns().length; i < len; i++) {
            Column column = index.getColumns()[i];
            createSb.append("    v").append(i).append(" ").append(column.getType().getFullName());
            if (i == len - 1)
                createSb.append('\n');
            else
                createSb.append(",\n");
        }
        createSb.append(")");

        StringBuilder indexSb = new StringBuilder("create unique index idx_").append(tableName).append(" on ").append(tableName).append("(index_name, ");
        for (int i = 0; i < index.getColumns().length; i++) {
            Column column = index.getColumns()[i];
            indexSb.append("v").append(i);
            if (i == index.getColumns().length - 1)
                indexSb.append(')');
            else
                indexSb.append(", ");
        }


        return Arrays.asList(createSb.toString(), indexSb.toString());
    }

    public static void main(String[] args) {
        Table table = new Table("testtab");
        Index unique = new Index(table, new Column[]{new Column("id", Column.Type.VARCHAR1000)}, true);
        Index index = new Index(table, new Column[]{new Column("emp_id", Column.Type.VARCHAR1000), new Column("create_tm", Column.Type.DATATIME)});

        buildSQL4CreateIndexTable(unique).forEach(System.out::println);
        buildSQL4CreateIndexTable(index).forEach(System.out::println);
    }

}
