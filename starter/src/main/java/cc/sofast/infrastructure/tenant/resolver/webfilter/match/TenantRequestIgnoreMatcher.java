package cc.sofast.infrastructure.tenant.resolver.webfilter.match;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 忽略某些url不需要解析租户信息
 *
 * @author apple
 */
public interface TenantRequestIgnoreMatcher {

    default boolean ignoreMatch(HttpServletRequest request) {

        return true;
    }
}
