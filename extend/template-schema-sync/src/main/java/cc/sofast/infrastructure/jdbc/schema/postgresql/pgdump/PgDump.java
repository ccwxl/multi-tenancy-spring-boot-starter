package cc.sofast.infrastructure.jdbc.schema.postgresql.pgdump;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import cc.sofast.infrastructure.jdbc.schema.postgresql.PostgresqlCommand;
import cc.sofast.infrastructure.jdbc.schema.utils.OSUtils;

/**
 * pg_dump --dbname=postgresql://gis_cache:gis_cache@192.168.8.83:5432/gis_cache?application_name=myapp -s -Fp -n public
 */
public class PgDump implements PostgresqlCommand {

    private static final String PG_DUMP_UNIX = "pg_dump ";
    private static final String PG_DUMP_WIN = "pg_dump.exe";
    private static final String CONNECTION_STRINGS = " --dbname=postgresql://%s:%s@%s:%s/%s?application_name=schema_sync_app ";
    private static final String PARAM = "-s -O -x -Fp -n %s ";

    @Override
    public String builder(SchemaInfo schema, String binDir) {
        StringBuilder command = new StringBuilder(binDir);
        if (OSUtils.isWindows()) {
            command.append(PG_DUMP_WIN);
        } else {
            command.append(PG_DUMP_UNIX);
        }
        String connStr = String.format(CONNECTION_STRINGS, schema.getUsername(), schema.getPassword(), schema.getHost(), schema.getPort(), schema.getDb());
        command.append(connStr);
        String schemaParam = String.format(PARAM, schema.getSchema());
        command.append(schemaParam);
        return command.toString();
    }
}
