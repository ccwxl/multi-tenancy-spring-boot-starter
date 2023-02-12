package cc.sofast.infrastructure.tenant.resolver.webfilter;

import cc.sofast.infrastructure.tenant.context.TenantContextHolder;
import cc.sofast.infrastructure.tenant.resolver.http.HttpRequestTenantResolver;
import cc.sofast.infrastructure.tenant.resolver.webfilter.match.TenantRequestMatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author apple
 */
public class TenantFilter extends OncePerRequestFilter implements Ordered {

    private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);

    private final TenantRequestMatcher requestMatcher;
    private final HttpRequestTenantResolver resolver;

    public TenantFilter(TenantRequestMatcher requestMatcher,
                        HttpRequestTenantResolver resolver) {
        this.requestMatcher = requestMatcher;
        this.resolver = resolver;
    }

    @Override
    public int getOrder() {

        return Integer.MAX_VALUE;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tenant = null;
        if (requestMatcher.match(request)) {
            tenant = (String) resolver.resolveTenantIdentifier(request);
        }
        try {
            if (StringUtils.hasLength(tenant)) {
                TenantContextHolder.push(tenant);
            } else {
                log.warn("current request notfound tenant request matched");
            }
        } finally {
            TenantContextHolder.clear();
        }
    }
}
