package cc.sofast.infrastructure.tenant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationTests {

    @Test
    void loadTenantFormProperties_Test() {
        ConfigurableApplicationContext case01 = new SpringApplicationBuilder()
//                .profiles("case_01")
                .main(StartupApp.class)
                .sources(StartupApp.class)
                .build()
                .run();

    }
}
