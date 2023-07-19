package cc.sofast.infrastructure.jdbc.schema.pgdump;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import cc.sofast.infrastructure.jdbc.schema.model.PgDumpResponse;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author apple
 */
public interface PgDump {

    PgDumpResponse dump(SchemaInfo option);

    default void dump(SchemaInfo option, File file) throws IOException {
        PgDumpResponse diff = dump(option);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            //TODO result null？
            StreamUtils.copy(diff.getResult().getBytes(), fileOutputStream);
        }
    }

}
