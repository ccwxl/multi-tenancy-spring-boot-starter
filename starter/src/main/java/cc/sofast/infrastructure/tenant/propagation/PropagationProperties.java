package cc.sofast.infrastructure.tenant.propagation;

import cc.sofast.infrastructure.tenant.support.Const;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author apple
 */
@ConfigurationProperties(prefix = PropagationProperties.PREFIX)
public class PropagationProperties {
    public static final String PREFIX = "spring.multitenancy.propagation";

    public String id = Const.TENANT_ID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
