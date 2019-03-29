package org.w01f.dds.layer4.index;

import org.w01f.dds.layer2.index.config.Column;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SQLBuildUtils {

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
        v0          varchar(1000),
        v1          datetime,
        id          varchar(200)
    );
    create unique index idx_u_sd on u_sd(index_name, v1, v2);
     */
    public static String buildIndexTableName(Index index) {
        return (index.isUnique() ? "u_" : "i_") + Arrays.asList(index.getColumns()).stream()
                .map(c -> c.getType().getSimpleName()).collect(Collectors.joining());
    }

    public static List<String> sql4CreateIndexTable(Index index) {
        String tableName = buildIndexTableName(index);
        StringBuilder createSb = new StringBuilder("create table ").append(tableName).append(" (\n");
        createSb.append("    index_name varchar(1000),\n");
        for (int i = 0, len = index.getColumns().length; i < len; i++) {
            Column column = index.getColumns()[i];
            createSb.append("    v").append(i).append(" ").append(column.getType().getFullName());
            createSb.append(",\n");
        }
        createSb.append("    id varchar(200)\n)");

        String unique = index.isUnique() ? "unique " : "";
        StringBuilder indexSb = new StringBuilder("create ").append(unique).append("index idx_").append(tableName).append(" on ").append(tableName).append("(index_name, ");
        for (int i = 0, len = index.getColumns().length; i < len; i++) {
            Column column = index.getColumns()[i];
            indexSb.append("v").append(i);
            indexSb.append(i == len - 1 ? ')' : ", ");
        }
        StringBuilder idIdxSb = new StringBuilder("create index idx_id_").append(tableName).append(" on ").append(tableName).append("(id)");

        return Arrays.asList(createSb.toString(), indexSb.toString(), idIdxSb.toString());
    }

    public static String sql4InsertIndex(Index index) {
        String tableName = buildIndexTableName(index);
        StringBuilder sb = new StringBuilder("insert into ").append(tableName).append("(index_name, ");
        for (int i = 0, len = index.getColumns().length; i < len; i++) {
            sb.append('v').append(i).append(", ");
        }
        sb.append("id) values (");
        for (int i = 0, len = index.getColumns().length; i < len; i++) {
            sb.append("?, ");
        }
        sb.append("?, ?)");
        return sb.toString();
    }

    public static String sql4DeleteIndex(Index index) {
        String tableName = buildIndexTableName(index);
        StringBuilder sb = new StringBuilder("delete from ").append(tableName).append(" where id = ? and index_name = ?");
        for (int i = 0, len = index.getColumns().length; i < len; i++) {
            sb.append(" and v").append(i).append(" = ?");
        }
        return sb.toString();
    }

    public static String sql4QueryIndex(Index index) {
        String tableName = buildIndexTableName(index);
        StringBuilder sb = new StringBuilder("select id from ").append(tableName).append(" where index_name = ?");
        StringBuilder order = new StringBuilder(" order by id");
        for (int i = 0, len = index.getColumns().length; i < len; i++) {
            sb.append(" and v").append(i).append(" = ?");
            order.append(", v").append(i);
        }
        return sb.append(order).toString();
    }

    public static void main(String[] args) {
        Table table = new Table("testtab");
        Index unique = new Index(table, new Column[]{new Column("id", Column.Type.VARCHAR1000)}, true);
        Index index = new Index(table, new Column[]{new Column("emp_id", Column.Type.VARCHAR1000), new Column("create_tm", Column.Type.DATATIME)});

        sql4CreateIndexTable(unique).forEach(System.out::println);
        sql4CreateIndexTable(index).forEach(System.out::println);

        System.out.println(sql4InsertIndex(unique));
        System.out.println(sql4InsertIndex(index));

        System.out.println(sql4DeleteIndex(unique));
        System.out.println(sql4DeleteIndex(index));

        System.out.println(sql4QueryIndex(unique));
        System.out.println(sql4QueryIndex(index));
    }
}
