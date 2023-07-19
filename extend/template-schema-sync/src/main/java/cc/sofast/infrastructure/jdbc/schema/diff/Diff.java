package cc.sofast.infrastructure.jdbc.schema.diff;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author apple
 */
public interface Diff {

    String diff(SchemaInfo source, SchemaInfo target);

    default void diff(SchemaInfo source, SchemaInfo target, File file) throws IOException {
        String diff = diff(source, target);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            StreamUtils.copy(diff.getBytes(), fileOutputStream);
        }
    }
}
