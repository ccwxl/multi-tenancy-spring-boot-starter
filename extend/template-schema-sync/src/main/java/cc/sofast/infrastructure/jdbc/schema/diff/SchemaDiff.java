package cc.sofast.infrastructure.jdbc.schema.diff;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import cc.sofast.infrastructure.jdbc.schema.postgresql.PostgresqlCommand;
import cc.sofast.infrastructure.jdbc.schema.postgresql.pgdump.PgDump;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author apple
 */
public class SchemaDiff implements Diff {

    @Override
    public String diff(SchemaInfo source, SchemaInfo target, String binFileDir) {
        PostgresqlCommand dump = new PgDump();
        String sourceSQL = dump.builder(source, binFileDir);
        String targetSQL = dump.builder(target, binFileDir);
        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .build();
        List<DiffRow> rows = generator.generateDiffRows(Arrays.asList(sourceSQL.split("\n")), Arrays.asList(targetSQL.split("\n")));
        StringBuilder diffStr = new StringBuilder();
        //TODO 需要重构
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

        // Format the parsed markdown 美化表格
        Formatter formatter = Formatter.builder().extensions(Collections.singleton(TablesExtension.create())).build();
        String formattedMarkdown = formatter.render(document);

        return formattedMarkdown;
    }
}
