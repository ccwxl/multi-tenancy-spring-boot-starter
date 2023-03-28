package cc.sofast.infrastructure.tenant.resolver.http;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;
import cc.sofast.infrastructure.tenant.resolver.TenantResolver;
import cc.sofast.infrastructure.tenant.resolver.TenantResolverProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author apple
 */
public class RequestPathTenantResolver extends AbstractHttpRequestTenantResolver {

    private final TenantResolverProperties properties;

    public RequestPathTenantResolver(TenantResolverProperties tenantResolverProperties) {
        this.properties = tenantResolverProperties;
    }

    @Override
    public Serializable resolveTenantIdentifier(HttpServletRequest request) throws TenantNotFoundException {
        String requestUrl = request.getRequestURI();
        String[] originalParts = StringUtils.tokenizeToStringArray(requestUrl, "/");
        if (originalParts.length >= properties.getWeb().getStripPrefix()) {
            return originalParts[properties.getWeb().getStripPrefix()];
        }
        throw new TenantNotFoundException("Tenant not resolver . tenant  value is null");
    }
}
