package org.w01f.dds.layer2.sql;

import org.w01f.dds.layer2.sql.parser.mysql.ParserUtils;

public class TestStructurePrint {
    public static void main(String[] args) {
        System.out.println(ParserUtils.parse("delete from ttt t where id = ?").structure());
        System.out.println(ParserUtils.parse("select id, name from ttt where id = ?").structure());
        System.out.println(ParserUtils.parse("update ttt set name = ? where id = ?").structure());
        System.out.println(ParserUtils.parse("insert into ttt (id, name) values (?, ?)").structure());
        System.out.println(ParserUtils.parse("SELECT * FROM t1 LEFT JOIN (t2 CROSS JOIN t3 CROSS JOIN t4) ON (t2.a=t1.a AND t3.b=t1.b AND t4.c=t1.c)").structure());
    }
}
