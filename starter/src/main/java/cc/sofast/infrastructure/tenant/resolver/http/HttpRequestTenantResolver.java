package cc.sofast.infrastructure.tenant.resolver.http;

import cc.sofast.infrastructure.tenant.exception.TenantNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;

/**
 * @author apple
 */
public interface HttpRequestTenantResolver {

    /**
     * 从http请求中解析当前租户
     *
     * @param request {@link HttpServletRequest}
     * @return Serializable CurrentTenantId
     * @throws TenantNotFoundException not found tenant in the request
     */
    Serializable resolveTenantIdentifier(HttpServletRequest request) throws TenantNotFoundException;

}
