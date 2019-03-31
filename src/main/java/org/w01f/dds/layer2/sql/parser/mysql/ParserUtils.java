package org.w01f.dds.layer2.sql.parser.mysql;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.antlr.v4.runtime.CodePointBuffer;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.w01f.dds.layer1.dsproxy.param.Param;
import org.w01f.dds.layer2.index.IndexConfigUtils;
import org.w01f.dds.layer2.index.config.Index;
import org.w01f.dds.layer2.index.config.Table;
import org.w01f.dds.layer2.sql.parser.mysql.antlr4.MySQLLexer;
import org.w01f.dds.layer2.sql.parser.mysql.antlr4.MySQLParser;
import org.w01f.dds.layer2.sql.parser.mysql.tree.*;

import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ParserUtils {

    private static Cache<String, StatNode> sqlCache = CacheBuilder.newBuilder().maximumSize(100000).build();

    public static StatNode parse(String sql) {
        try {
            StatNode origin = sqlCache.get(sql, () -> {
                CodePointBuffer codePointBuffer = CodePointBuffer.withBytes(ByteBuffer.wrap(sql.getBytes()));
                CodePointCharStream codePointCharStream = CodePointCharStream.fromBuffer(codePointBuffer);
                MySQLLexer lexer = new MySQLLexer(codePointCharStream);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                MySQLParser parser = new MySQLParser(tokens);
                parser.setErrorHandler(new MySQLParserErrorStrategy());
                ParseTree tree = parser.stat();

                return (StatNode) new MySQLVisitorImpl().visit(tree);
            });
            return (StatNode) origin.clone();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Index, Param[]>  parseInsertIndex(InsertNode insert) {
        List<String> names = null;
        List<ElementPlaceholderNode> elements = new ArrayList<>();

        { // check insert structure.
            if (insert.getSelect() != null) {
                throw new RuntimeException("only support value insert mode, such as [insert into tab(a, b, c) values(?, ? ,?)]");
            }
            ColumnNamesNode columnNames = insert.getColumnNames();
            if (columnNames != null) {
                names = columnNames.getNames();
            }
            if (names == null || names.size() == 0) {
                throw new RuntimeException("column names cannot be empty.");
            }
            for (ElementNode element : insert.getValueNames().getElements()) {
                if (element instanceof ElementPlaceholderNode) {
                    elements.add(((ElementPlaceholderNode) element));
                } else {
                    throw new RuntimeException("value list only support placeholder list.");
                }
            }
        }

        Table table = IndexConfigUtils.getTableConfig(insert.getTableName());
        List<Index> indices = table.getIndices();
        Map<Index, Param[]> indexSetterMap = new HashMap<>();
        Param idParam = elements.get(names.indexOf("id")).getParam();

        for (Index index : indices) {
            int len = index.getColumns().length;
            Param[] params = new Param[len + 1];
            for (int i = 0; i < len; i++) {
                int si= names.indexOf(index.getColumns()[i].getName());
                params[i] = elements.get(si).getParam();
            }
            params[len] = idParam;

            indexSetterMap.put(index, params);
        }

        return indexSetterMap;
    }
}
