package cc.sofast.infrastructure.tenant.propagation;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author apple
 */
public class RestTemplateTenantPropagationInterceptorAfterPropertiesSet implements InitializingBean {

    @Autowired(required = false)
    private Collection<RestTemplate> restTemplates;

    @Autowired
    private RestTemplateTenantPropagationInterceptor restTemplateTenantPropagationInterceptor;

    @Override
    public void afterPropertiesSet() {
        if (this.restTemplates != null) {
            for (RestTemplate restTemplate : restTemplates) {
                List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(
                        restTemplate.getInterceptors());
                interceptors.add(this.restTemplateTenantPropagationInterceptor);
                restTemplate.setInterceptors(interceptors);
            }
        }
    }
}
