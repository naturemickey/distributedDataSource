package org.w01f.dds.layer2.sql;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w01f.dds.layer2.sql.parser.mysql.MySQLParserErrorStrategy;
import org.w01f.dds.layer2.sql.parser.mysql.MySQLVisitorImpl;
import org.w01f.dds.layer2.sql.parser.mysql.antlr4.MySQLLexer;
import org.w01f.dds.layer2.sql.parser.mysql.antlr4.MySQLParser;
import org.w01f.dds.layer2.sql.parser.mysql.tree.SQLSyntaxTreeNode;

import java.io.ByteArrayInputStream;

public class ParserUtils {
    public static SQLSyntaxTreeNode parse(String sql) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(sql.getBytes());) {
            ANTLRInputStream input = new ANTLRInputStream(is);
            MySQLLexer lexer = new MySQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MySQLParser parser = new MySQLParser(tokens);
            parser.setErrorHandler(new MySQLParserErrorStrategy());
            ParseTree tree = parser.stat();

            return new MySQLVisitorImpl().visit(tree);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
