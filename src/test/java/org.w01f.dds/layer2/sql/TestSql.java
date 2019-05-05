package org.w01f.dds.layer2.sql;

import org.w01f.dds.layer2.sql.parser.mysql.ParserUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestSql {

    public static void main(String[] args) throws Exception{
        final byte[] bytes = Files.readAllBytes(Paths.get("src/test/testsql"));

        String sql = new String(bytes);


        System.out.println(ParserUtils.parse(sql));
    }
}
