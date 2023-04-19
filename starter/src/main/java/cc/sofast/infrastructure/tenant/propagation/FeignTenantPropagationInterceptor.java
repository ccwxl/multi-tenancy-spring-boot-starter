package cc.sofast.infrastructure.tenant.propagation;

import cc.sofast.infrastructure.tenant.TenantContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;

/**
 * @author apple
 */
public class FeignTenantPropagationInterceptor implements RequestInterceptor {
    private final PropagationProperties properties;

    public FeignTenantPropagationInterceptor(PropagationProperties properties) {
        this.properties = properties;
    }

    @Override
    public void apply(RequestTemplate template) {
        String tenant = TenantContextHolder.peek();
        if (StringUtils.hasLength(tenant)) {
            template.header(properties.getId(), tenant);
        }
    }
}
