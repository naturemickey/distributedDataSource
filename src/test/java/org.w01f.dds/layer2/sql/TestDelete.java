package org.w01f.dds.layer2.sql;

import org.w01f.dds.layer2.sql.parser.mysql.ParserUtils;

import java.io.IOException;

public class TestDelete {
	public static void main(String[] args) throws Exception {

		for (String sql : new String[] { //
				"delete from tt_order_status where id = ?", //
				"delete from tt_order_status t where t.id = ?", //
				"delete from tt_order_status as t -- test comment", //
				"delete from tt_order_status lImIt 9 # test comment", //
				"delete from tt_order_status lImIt ? # test comment", //
				"DELETE FROM t1 WHERE s11 > ANY (SELECT COUNT(*) /* no hint */ FROM t2 WHERE NOT EXISTS (SELECT * FROM t3 WHERE ROW(5*t2.s1,77)= (SELECT 50,11*s1 FROM t4 UNION SELECT 50,77 FROM(SELECT * FROM t5) AS t5)))",//
				}) {
			fun(sql);
		}

	}

	private static void fun(String sql) throws IOException {
		System.out.println(ParserUtils.parse(sql));
	}
}
