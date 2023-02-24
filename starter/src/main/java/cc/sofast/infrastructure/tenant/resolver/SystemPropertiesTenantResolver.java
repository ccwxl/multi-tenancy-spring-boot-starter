package cc.sofast.infrastructure.tenant.resolver;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;

import java.io.Serializable;

/**
 * @author apple
 */
public class SystemPropertiesTenantResolver implements TenantResolver {

    private final TenantResolverProperties tenantResolverProperties;

    public SystemPropertiesTenantResolver(TenantResolverProperties tenantResolverProperties) {
        this.tenantResolverProperties = tenantResolverProperties;
    }

    @Override
    public Serializable resolveTenantIdentifier() throws TenantNotFoundException {

        return System.getProperty(tenantResolverProperties.getSystem().getId(), "default");
    }
}
