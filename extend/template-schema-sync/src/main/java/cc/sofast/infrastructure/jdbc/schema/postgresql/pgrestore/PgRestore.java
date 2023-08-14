package cc.sofast.infrastructure.jdbc.schema.postgresql.pgrestore;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import cc.sofast.infrastructure.jdbc.schema.postgresql.PostgresqlCommand;
import cc.sofast.infrastructure.jdbc.schema.utils.OSUtils;

/**
 * @author apple
 */
public class PgRestore implements PostgresqlCommand {

    private static final String PG_RESTORE = "pg_restore ";
    private static final String PG_RESTORE_EXE = "pg_restore.exe";
    private static final String CONNECTION_STRINGS = " --dbname=postgresql://%s:%s@%s:%s/%s?application_name=schema_sync_app ";
    private static final String PARAM = "-s -O -x -Fp -n %s ";

    @Override
    public String builder(SchemaInfo schema, String binDir) {
        StringBuilder command = new StringBuilder(binDir);
        if (OSUtils.isWindows()) {
            command.append(PG_RESTORE_EXE);
        } else {
            command.append(PG_RESTORE);
        }
        String connStr = String.format(CONNECTION_STRINGS, schema.getUsername(), schema.getPassword(), schema.getHost(), schema.getPort(), schema.getDb());
        command.append(connStr);
        String schemaParam = String.format(PARAM, schema.getSchema());
        command.append(schemaParam);
        return command.toString();
    }
}
