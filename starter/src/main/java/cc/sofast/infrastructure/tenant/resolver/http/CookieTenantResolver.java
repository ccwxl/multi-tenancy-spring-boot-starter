package cc.sofast.infrastructure.tenant.resolver.http;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;
import cc.sofast.infrastructure.tenant.resolver.TenantResolver;
import cc.sofast.infrastructure.tenant.resolver.TenantResolverProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author apple
 */
public class CookieTenantResolver extends AbstractHttpRequestTenantResolver {

    private final TenantResolverProperties properties;

    public CookieTenantResolver(TenantResolverProperties tenantResolverProperties) {
        this.properties = tenantResolverProperties;
    }


    @Override
    public Serializable resolveTenantIdentifier(HttpServletRequest request) throws TenantNotFoundException {
        if (request.getCookies() != null) {
            Optional<Cookie> optionalTenantId = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(properties.getWeb().getId())).findFirst();
            if (optionalTenantId.isPresent()) {
                return optionalTenantId.get().getValue();
            }
        }
        throw new TenantNotFoundException("Tenant could not be resolved from the Cookie: " + properties.getWeb().getId());
    }
}
