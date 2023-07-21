package cc.sofast.infrastructure.jdbc.schema;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class SchemaTest {

    @Test
    public void TestPgDump() throws IOException {
        ProcessBuilder processBuilder =
                new ProcessBuilder("/Users/apple/opensource/multi-tenancy-spring-boot-starter/extend/template-schema-sync/src/main/resources/postgresql/postgres-darwin-x86_64/bin/pg_dump", "-version");
        // setting up a working directory
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); // 获取命令执行的结果流
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line); // 输出命令执行的结果
        }
        reader.close();
    }


    @Test
    public void testCreate() {
        Schema build = Schema.builder().build();
        build.init();
        System.out.println("ok");
    }
}