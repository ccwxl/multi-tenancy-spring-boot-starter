package cc.sofast.infrastructure.jdbc.schema;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author apple
 */
@Configuration
@EnableConfigurationProperties(TenantSchemaProperties.class)
public class TenantSchemaAutoconfiguration {

    @Bean(initMethod = "init")
    public Schema schema(TenantSchemaProperties tenantSchemaProperties) {

        return Schema.builder().build();
    }
}
