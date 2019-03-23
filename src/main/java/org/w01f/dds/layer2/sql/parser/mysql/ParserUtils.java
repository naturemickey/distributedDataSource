package org.w01f.dds.layer2.sql.parser.mysql;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w01f.dds.layer2.sql.parser.mysql.antlr4.MySQLLexer;
import org.w01f.dds.layer2.sql.parser.mysql.antlr4.MySQLParser;
import org.w01f.dds.layer2.sql.parser.mysql.tree.SQLSyntaxTreeNode;

import java.nio.ByteBuffer;

public class ParserUtils {

    private static Cache<String, SQLSyntaxTreeNode> sqlCache = CacheBuilder.newBuilder().maximumSize(100000).build();

    public static SQLSyntaxTreeNode parse(String sql) {
        try {
            SQLSyntaxTreeNode origin = sqlCache.get(sql, () -> {
                CodePointBuffer codePointBuffer = CodePointBuffer.withBytes(ByteBuffer.wrap(sql.getBytes()));
                CodePointCharStream codePointCharStream = CodePointCharStream.fromBuffer(codePointBuffer);
                MySQLLexer lexer = new MySQLLexer(codePointCharStream);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                MySQLParser parser = new MySQLParser(tokens);
                parser.setErrorHandler(new MySQLParserErrorStrategy());
                ParseTree tree = parser.stat();

                return new MySQLVisitorImpl().visit(tree);
            });
            return origin.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
