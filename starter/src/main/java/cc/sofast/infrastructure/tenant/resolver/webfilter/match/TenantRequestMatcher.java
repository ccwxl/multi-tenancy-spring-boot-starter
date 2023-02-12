package cc.sofast.infrastructure.tenant.resolver.webfilter.match;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author apple
 */
public interface TenantRequestMatcher {

    default boolean match(HttpServletRequest request) {

        return true;
    }

}
