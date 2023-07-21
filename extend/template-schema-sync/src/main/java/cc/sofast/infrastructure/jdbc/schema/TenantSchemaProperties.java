package cc.sofast.infrastructure.jdbc.schema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static cc.sofast.infrastructure.jdbc.schema.TenantSchemaProperties.PREFIX;

/**
 * @author apple
 */
@Getter
@Setter
@ConfigurationProperties(PREFIX)
public class TenantSchemaProperties {
    public static final String PREFIX = "spring.multitenancy.db.schema";

    /**
     * 默认为用户目录下 multitenancy 目录
     */
    private String workDir;
}
