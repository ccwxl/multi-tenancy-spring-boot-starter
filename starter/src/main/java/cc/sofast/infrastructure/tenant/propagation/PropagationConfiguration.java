package cc.sofast.infrastructure.tenant.propagation;

import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author apple
 */
@Configuration
@EnableConfigurationProperties(PropagationProperties.class)
public class PropagationConfiguration {

    @Configuration
    @ConditionalOnClass(Feign.class)
    public static class FeignPropagation {

        @Bean
        public FeignTenantPropagationInterceptor feignTenantPropagationInterceptor(PropagationProperties properties) {

            return new FeignTenantPropagationInterceptor(properties);
        }
    }

    @Configuration
    @ConditionalOnClass(RestTemplate.class)
    public static class RestTemplatePropagation {

        @Bean
        public RestTemplateTenantPropagationInterceptor restTemplateTenantPropagationInterceptor(PropagationProperties properties) {
            return new RestTemplateTenantPropagationInterceptor(properties);
        }

        @Bean
        public RestTemplateTenantPropagationInterceptorAfterPropertiesSet restTemplateTenantPropagationInterceptorAfterPropertiesSet() {
            return new RestTemplateTenantPropagationInterceptorAfterPropertiesSet();
        }
    }
}
