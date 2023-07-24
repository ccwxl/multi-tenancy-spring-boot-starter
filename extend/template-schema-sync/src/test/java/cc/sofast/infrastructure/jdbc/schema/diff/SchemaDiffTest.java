package cc.sofast.infrastructure.jdbc.schema.diff;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import cc.sofast.infrastructure.jdbc.schema.postgresql.pgdump.PgDump;
import cc.sofast.infrastructure.jdbc.schema.utils.PgDumpUtils;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class SchemaDiffTest {

    @Test
    void diffString() {
        String source = """
                --
                -- PostgreSQL database dump
                --
                                
                -- Dumped from database version 15.2 (Debian 15.2-1.pgdg100+1)
                -- Dumped by pg_dump version 15.1
                                
                SET statement_timeout = 0;
                SET lock_timeout = 0;
                """;
        String target = """
                --
                -- PostgreSQL database dump
                --
                                
                -- Dumped from database version 15.2 (Debian 15.2-1.pgdg100+1)
                -- Dumped by pg_dump version 15.1
                                
                SET lock_timeout = 1;
                """;

        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .build();
        List<DiffRow> rows = generator.generateDiffRows(Arrays.asList(source.split("\n")), Arrays.asList(target.split("\n")));

        StringBuilder diffStr = new StringBuilder();
        diffStr.append("|source|target|").append("\n");
        diffStr.append("|:--------|:---|").append("\n");
        for (DiffRow row : rows) {
            if (!row.getTag().equals(DiffRow.Tag.EQUAL)) {
                diffStr.append("|<font color=\"#da0000\">").append(row.getOldLine()).append("</font> |").append("<font color=\"#da0000\">").append(row.getNewLine()).append("</font> |").append("\n");
            } else {
                diffStr.append("| ").append(row.getOldLine()).append("|").append(row.getNewLine()).append(" |").append("\n");
            }
        }
        String markdownTable = diffStr.toString();
        // Parse the markdown
        Parser parser = Parser.builder().extensions(Collections.singleton(TablesExtension.create())).build();
        Node document = parser.parse(markdownTable);

        // Format the parsed markdown
        Formatter formatter = Formatter.builder().extensions(Collections.singleton(TablesExtension.create())).build();
        String formattedMarkdown = formatter.render(document);

        System.out.println(formattedMarkdown);

//        System.out.println(markdownTable);
    }

    @Test
    void testDiff() throws IOException {
        SchemaInfo source = new SchemaInfo();
        source.setHost("127.0.0.1");
        source.setPort(5432);
        source.setDb("postgres");
        source.setSchema("s1");
        source.setUsername("postgres");
        source.setPassword("postgres");

        SchemaInfo target = new SchemaInfo();
        target.setHost("127.0.0.1");
        target.setPort(5432);
        target.setDb("postgres");
        target.setSchema("s2");
        target.setUsername("postgres");
        target.setPassword("postgres");

        SchemaDiff schemaDiff = new SchemaDiff();
        String diff = schemaDiff.diff(source, target, PgDumpUtils.getPgDumpBinFileDir(""));
        System.out.println(diff);
    }
}