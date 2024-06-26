package cc.sofast.infrastructure.tenant.resolver.http;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;
import cc.sofast.infrastructure.tenant.resolver.TenantResolver;
import cc.sofast.infrastructure.tenant.resolver.TenantResolverProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;

/**
 * @author apple
 */
public class HeaderTenantResolver extends AbstractHttpRequestTenantResolver {

    private final TenantResolverProperties properties;

    public HeaderTenantResolver(TenantResolverProperties properties) {
        this.properties = properties;
    }

    @Override
    public Serializable resolveTenantIdentifier(HttpServletRequest request) throws TenantNotFoundException {
        String id = properties.getWeb().getId();
        String tenant = request.getHeader(id);
        if (!StringUtils.hasLength(tenant)) {
            throw new TenantNotFoundException("Tenant not resolver in header " + id + " value is null current URL: " + request.getRequestURL());
        }
        return tenant;
    }
}
