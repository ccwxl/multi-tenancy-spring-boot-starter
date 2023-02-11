package cc.sofast.infrastructure.tenant.webfilter.match;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;

/**
 * @author apple
 */
public class HttpPathPatterRequestMatcher implements TenantRequestMatcher {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean match(HttpServletRequest request) {


        return TenantRequestMatcher.super.match(request);
    }
}
