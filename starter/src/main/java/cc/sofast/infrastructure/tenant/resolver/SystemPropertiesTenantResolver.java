package cc.sofast.infrastructure.tenant.resolver;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;

import java.io.Serializable;

/**
 * @author apple
 */
public class SystemPropertiesTenantResolver implements TenantResolver {

    @Override
    public Serializable resolveTenantIdentifier() throws TenantNotFoundException {

        return null;
    }
}
