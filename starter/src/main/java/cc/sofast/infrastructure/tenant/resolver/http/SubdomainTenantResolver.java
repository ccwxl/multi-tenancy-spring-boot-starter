package cc.sofast.infrastructure.tenant.resolver.http;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;
import cc.sofast.infrastructure.tenant.resolver.TenantResolver;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;

public class SubdomainTenantResolver implements HttpRequestTenantResolver {

    @Override
    public Serializable resolveTenantIdentifier(HttpServletRequest request) throws TenantNotFoundException {

        return null;
    }

    @Override
    public Serializable resolveTenantIdentifier() throws TenantNotFoundException {

        return null;
    }
}
