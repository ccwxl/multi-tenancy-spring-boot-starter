package cc.sofast.infrastructure.tenant.resolver.http;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;
import cc.sofast.infrastructure.tenant.resolver.TenantResolver;
import cc.sofast.infrastructure.tenant.resolver.TenantResolverProperties;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;

/**
 * @author apple
 */
public class CookieTenantResolver implements HttpRequestTenantResolver {

    public CookieTenantResolver(TenantResolverProperties tenantResolverProperties) {
        
    }

    @Override
    public Serializable resolveTenantIdentifier() throws TenantNotFoundException {

        return null;
    }

    @Override
    public Serializable resolveTenantIdentifier(HttpServletRequest request) throws TenantNotFoundException {

        return null;
    }
}
