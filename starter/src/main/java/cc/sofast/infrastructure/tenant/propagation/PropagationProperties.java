package cc.sofast.infrastructure.tenant.propagation;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author apple
 */
@ConfigurationProperties(prefix = PropagationProperties.PREFIX)
public class PropagationProperties {
    public static final String PREFIX = "sofast.tenant.propagation";

    public String id = "X-Tenant";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
