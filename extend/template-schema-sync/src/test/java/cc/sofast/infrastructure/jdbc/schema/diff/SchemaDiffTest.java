package cc.sofast.infrastructure.jdbc.schema.diff;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class SchemaDiffTest {

    @Test
    void diff() {

    }

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
}