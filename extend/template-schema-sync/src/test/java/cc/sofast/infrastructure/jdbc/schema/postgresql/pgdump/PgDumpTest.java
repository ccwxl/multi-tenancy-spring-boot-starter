package cc.sofast.infrastructure.jdbc.schema.postgresql.pgdump;

import cc.sofast.infrastructure.jdbc.schema.Schema;
import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import cc.sofast.infrastructure.jdbc.schema.utils.OSUtils;
import cc.sofast.infrastructure.jdbc.schema.utils.PgDumpUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class PgDumpTest {

    @Test
    void builder() throws IOException {
        Schema schema = Schema.builder().build();
        SchemaInfo schemaInfo = new SchemaInfo();
        schemaInfo.setUsername("gis_cache");
        schemaInfo.setPassword("gis_cache");
        schemaInfo.setHost("192.168.8.83");
        schemaInfo.setPort(5432);
        schemaInfo.setDb("gis_cache");
        schemaInfo.setSchema("public");
        PgDump pgDump = new PgDump();
        String command = pgDump.builder(schemaInfo, PgDumpUtils.getPgDumpBinFileDir(schema.getBaseDir()));
        String result = OSUtils.exeCmd(command);
        System.out.println(result);
        Assertions.assertNotNull(result);
    }
}