package cc.sofast.infrastructure.tenant.resolver.webfilter;

import cc.sofast.infrastructure.tenant.context.TenantContextHolder;
import cc.sofast.infrastructure.tenant.resolver.TenantResolver;
import cc.sofast.infrastructure.tenant.resolver.webfilter.match.TenantRequestIgnoreMatcher;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author apple
 */
public class TenantContextFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(TenantContextFilter.class);
    private final TenantRequestIgnoreMatcher requestMatcher;
    private final TenantResolver resolver;

    public TenantContextFilter(TenantRequestIgnoreMatcher requestMatcher,
                               TenantResolver resolver) {
        this.requestMatcher = requestMatcher;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String tenant = null;
        if (!requestMatcher.ignoreMatch(request)) {
            tenant = (String) resolver.resolveTenantIdentifier();
        }

        if (StringUtils.hasLength(tenant)) {
            TenantContextHolder.push(tenant);
        } else {
            log.warn("current request notfound tenant request matched");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }
}
