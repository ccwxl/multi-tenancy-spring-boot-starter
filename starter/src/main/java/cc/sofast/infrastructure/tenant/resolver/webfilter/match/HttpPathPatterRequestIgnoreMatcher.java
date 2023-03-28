package cc.sofast.infrastructure.tenant.resolver.webfilter.match;

import cc.sofast.infrastructure.tenant.resolver.TenantResolverProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author apple
 */
public class HttpPathPatterRequestIgnoreMatcher implements TenantRequestIgnoreMatcher {

    private final TenantResolverProperties properties;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public HttpPathPatterRequestIgnoreMatcher(TenantResolverProperties tenantResolverProperties) {
        this.properties = tenantResolverProperties;
    }

    @Override
    public boolean ignoreMatch(HttpServletRequest request) {
        boolean matched = false;
        if (CollectionUtils.isEmpty(properties.getIgnorePath())) {
            return matched;
        }
        List<String> ignorePath = new ArrayList<>(properties.getIgnorePath());
        String requestUrl = request.getRequestURI();
        for (String path : ignorePath) {
            if (pathMatcher.match(path, requestUrl)) {
                matched = true;
                break;
            }
        }
        return matched;
    }
}
