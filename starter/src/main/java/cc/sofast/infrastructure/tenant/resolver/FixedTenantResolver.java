package cc.sofast.infrastructure.tenant.resolver;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;

import java.io.Serializable;

/**
 * @author apple
 */
public class FixedTenantResolver implements TenantResolver {

    private TenantResolverProperties tenantResolverProperties;

    public FixedTenantResolver(TenantResolverProperties tenantResolverProperties) {
        this.tenantResolverProperties = tenantResolverProperties;
    }

    @Override
    public Serializable resolveTenantIdentifier() throws TenantNotFoundException {

        return tenantResolverProperties.getFixed().getValue();
    }
}
