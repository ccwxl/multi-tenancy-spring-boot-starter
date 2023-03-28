package cc.sofast.infrastructure.tenant.resolver.http;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;
import cc.sofast.infrastructure.tenant.resolver.TenantResolver;
import cc.sofast.infrastructure.tenant.resolver.TenantResolverProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.io.Serializable;

/**
 * @author apple
 */
public class SubdomainTenantResolver extends AbstractHttpRequestTenantResolver {

    private final TenantResolverProperties properties;

    public SubdomainTenantResolver(TenantResolverProperties tenantResolverProperties) {
        this.properties = tenantResolverProperties;
    }

    @Override
    public Serializable resolveTenantIdentifier(HttpServletRequest request) throws TenantNotFoundException {
        if (request.getHeaderNames() != null) {
            String host = request.getHeader(HttpHeaders.HOST);
            if (host != null) {
                if (host.contains("/")) {
                    host = host.substring(host.indexOf("/") + 2);
                }
                if (host.contains(".")) {
                    return host.substring(0, host.indexOf("."));
                }
            }
            return TenantResolver.DEFAULT;
        }
        return TenantResolver.DEFAULT;
    }
}
