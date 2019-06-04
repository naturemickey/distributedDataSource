package org.w01f.dds.layer2.sql;

import org.w01f.dds.layer2.sql.parser.mysql.ParserUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestSql {

    public static void main(String[] args) throws Exception{
//        final byte[] bytes = Files.readAllBytes(Paths.get("src/test/testsql"));

//        String sql = new String(bytes);

//        String sql = "insert into tt_special_config(id,values,type,config_id,value,created_time,modified_time,is_deleted) value(?,?)";
        String sql = "select where, values from tab";

        System.out.println(ParserUtils.parse(sql));
    }
}
