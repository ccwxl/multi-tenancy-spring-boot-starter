package cc.sofast.infrastructure.tenant.resolver;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;

import java.io.Serializable;

/**
 * @author apple
 */
public interface TenantResolver {

    /**
     * The name of the default tenant.
     */
    String DEFAULT = "def";

    Serializable resolveTenantIdentifier() throws TenantNotFoundException;
}
