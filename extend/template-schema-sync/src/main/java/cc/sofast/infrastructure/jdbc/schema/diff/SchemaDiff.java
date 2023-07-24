package cc.sofast.infrastructure.jdbc.schema.diff;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import cc.sofast.infrastructure.jdbc.schema.postgresql.PostgresqlCommand;
import cc.sofast.infrastructure.jdbc.schema.postgresql.pgdump.PgDump;
import cc.sofast.infrastructure.jdbc.schema.utils.OSUtils;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.formatter.Formatter;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author apple
 */
public class SchemaDiff implements Diff {

    private static final String SPACE = "   ";
    private static final String TB_HEADER = "|:--------|:---|";
    private static final String DELIMITER = "|";
    private static final String DIFF_STYLE_START = "<font color=\"#da0000\">";
    private static final String DIFF_STYLE_END = "</font>";
    private static final String WRAP = "\n";

    @Override
    public String diff(SchemaInfo source, SchemaInfo target, String binFileDir) throws IOException {
        PostgresqlCommand dump = new PgDump();
        String sourceCommand = dump.builder(source, binFileDir);
        String targetCommand = dump.builder(target, binFileDir);

        String sourceText = OSUtils.exeCmd(sourceCommand);
        String targetText = OSUtils.exeCmd(targetCommand);

        DiffRowGenerator generator = DiffRowGenerator.create()
                .showInlineDiffs(true)
                .inlineDiffByWord(true)
                .build();

        //将Schema 进行替换，避免太多的diff 影响
        sourceText = sourceText.replaceAll(source.getSchema(), "{replace}");
        targetText = targetText.replaceAll(target.getSchema(), "{replace}");

        List<DiffRow> rows = generator.generateDiffRows(Arrays.asList(sourceText.split(WRAP)), Arrays.asList(targetText.split(WRAP)));
        StringBuilder diffStr = new StringBuilder();
        diffStr.append(DELIMITER).append(source.getSchema()).append(DELIMITER).append(target.getSchema()).append(DELIMITER).append(WRAP);
        diffStr.append(TB_HEADER).append(WRAP);
        for (DiffRow row : rows) {
            if (!row.getTag().equals(DiffRow.Tag.EQUAL)) {
                diffStr.append(DELIMITER)
                        .append(DIFF_STYLE_START).append(row.getOldLine()).append(DIFF_STYLE_END).append(DELIMITER)
                        .append(DIFF_STYLE_START).append(row.getNewLine()).append(DIFF_STYLE_END).append(DELIMITER)
                        .append(WRAP);
            } else {
                diffStr.append(DELIMITER).append(SPACE).append(row.getOldLine())
                        .append(DELIMITER).append(SPACE).append(row.getNewLine())
                        .append(DELIMITER).append(WRAP);
            }
        }
        String markdownTable = diffStr.toString();
        // Parse the markdown
        Parser parser = Parser.builder().extensions(Collections.singleton(TablesExtension.create())).build();
        Node document = parser.parse(markdownTable);
        // Format the parsed markdown 美化表格
        Formatter formatter = Formatter.builder().extensions(Collections.singleton(TablesExtension.create())).build();
        return formatter.render(document);
    }
}
