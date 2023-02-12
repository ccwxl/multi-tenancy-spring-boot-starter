package cc.sofast.infrastructure.tenant.propagation;

import cc.sofast.infrastructure.tenant.datasource.TenantDataSourceProperties;
import cc.sofast.infrastructure.tenant.context.TenantContextHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author apple
 */
public class RestTemplateTenantPropagationInterceptor implements ClientHttpRequestInterceptor {

    private final PropagationProperties properties;

    public RestTemplateTenantPropagationInterceptor(PropagationProperties properties) {
        this.properties = properties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        String tenant = TenantContextHolder.peek();
        if (StringUtils.hasLength(tenant)) {
            requestWrapper.getHeaders().add(properties.getId(), tenant);
        }
        return execution.execute(requestWrapper, body);
    }
}
