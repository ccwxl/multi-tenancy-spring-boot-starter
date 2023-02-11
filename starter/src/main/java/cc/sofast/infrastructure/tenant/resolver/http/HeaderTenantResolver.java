package cc.sofast.infrastructure.tenant.resolver.http;

import cc.sofast.infrastructure.tenant.datasource.TenantDataSourceProperties;
import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;
import cc.sofast.infrastructure.tenant.resolver.TenantResolver;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;

public class HeaderTenantResolver implements HttpRequestTenantResolver, TenantResolver {

    private final TenantDataSourceProperties properties;

    public HeaderTenantResolver(TenantDataSourceProperties properties) {
        this.properties = properties;
    }

    @Override
    public Serializable resolveTenantIdentifier(HttpServletRequest request) throws TenantNotFoundException {
        String tenant = request.getHeader(properties.getIdentification());
        if (!StringUtils.hasLength(tenant)) {
            throw new TenantNotFoundException("Tenant not resolver in header " + properties.getIdentification() + " value is null");
        }
        return tenant;
    }

    @Override
    public Serializable resolveTenantIdentifier() throws TenantNotFoundException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return resolveTenantIdentifier(request);
        }
        throw new TenantNotFoundException("Tenant not resolver in header " + properties.getIdentification() + " value is null");
    }
}
