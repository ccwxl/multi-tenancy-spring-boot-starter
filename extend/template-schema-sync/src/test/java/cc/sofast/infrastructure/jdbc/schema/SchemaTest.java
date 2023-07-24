package cc.sofast.infrastructure.jdbc.schema;

import cc.sofast.infrastructure.jdbc.schema.utils.OSUtils;
import cc.sofast.infrastructure.jdbc.schema.utils.PgDumpUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class SchemaTest {

    @Test
    void TestPgDump() throws IOException {
        String s = OSUtils.exeCmd("/home/xielong.wang/multitenancy/postgres-linux-x86_64/bin/pg_dump --dbname=postgresql://gis_cache:gis_cache@192.168.8.83:5432/gis_cache?application_name=myapp -s -Fp -n public");
        Assertions.assertNotNull(s);
    }

    @Test
    void testCreate() {
        Schema build = Schema.builder().build();
        build.init();
        Assertions.assertNotNull(PgDumpUtils.getPgDumpBinPath(build.getBaseDir()));
    }
}