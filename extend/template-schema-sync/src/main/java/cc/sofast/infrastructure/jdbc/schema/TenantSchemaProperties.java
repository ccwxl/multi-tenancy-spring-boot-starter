package cc.sofast.infrastructure.jdbc.schema;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static cc.sofast.infrastructure.jdbc.schema.TenantSchemaProperties.PREFIX;

/**
 * @author apple
 */
@ConfigurationProperties(PREFIX)
public class TenantSchemaProperties {
    public static final String PREFIX = "spring.multitenancy.db.schema";

    /**
     * 默认为
     */
    private String workDir;

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }
}
