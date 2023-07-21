package cc.sofast.infrastructure.jdbc.schema;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author apple
 */
@Configuration
@EnableConfigurationProperties(TenantSchemaProperties.class)
public class TenantSchemaAutoconfiguration {

    @Bean(initMethod = "init")
    @Lazy
    public Schema schema(TenantSchemaProperties tenantSchemaProperties) {

        return Schema.builder().build();
    }
}
