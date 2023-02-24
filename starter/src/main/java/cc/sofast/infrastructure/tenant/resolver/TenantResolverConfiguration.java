package cc.sofast.infrastructure.tenant.resolver;

import cc.sofast.infrastructure.tenant.resolver.http.*;
import cc.sofast.infrastructure.tenant.resolver.webfilter.TenantFilter;
import cc.sofast.infrastructure.tenant.resolver.webfilter.match.HttpPathPatterRequestMatcher;
import cc.sofast.infrastructure.tenant.resolver.webfilter.match.TenantRequestMatcher;
import cc.sofast.infrastructure.tenant.support.ResolverEnvironmentPropertyUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author apple
 * <p>
 * 解析方式有多种:
 * 1. webfilter下有多种解析方式:
 * @see HeaderTenantResolver
 * @see CookieTenantResolver
 * @see RequestPathTenantResolver
 * @see SubdomainTenantResolver
 * 2. fixed
 * 3. systeProperties
 * </p>
 */
@Configuration
@EnableConfigurationProperties(TenantResolverProperties.class)
public class TenantResolverConfiguration {

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class TenantFilterRegister {


        @Bean
        public TenantFilter tenantFilterRegister(TenantRequestMatcher tenantRequestMatcher,TenantResolver tenantResolver) {

            return new TenantFilter(tenantRequestMatcher,tenantResolver);
        }

        @Bean
        public TenantRequestMatcher tenantRequestMatcher() {

            return new HttpPathPatterRequestMatcher();
        }
    }

    @Configuration
    @Conditional(ResolverWebTypeConfigurationCondition.class)
    public static class HttpTenantResolverConfiguration {

        @Bean
        @Conditional(HeaderConfigurationCondition.class)
        public HttpRequestTenantResolver httpRequestTenantResolver(TenantResolverProperties tenantResolverProperties) {

            return new HeaderTenantResolver(tenantResolverProperties);
        }

    }

    @Configuration
    @Conditional(ResolverFixedTypeConfigurationCondition.class)
    public static class FixTenantResolverConfiguration {
        @Bean
        public TenantResolver tenantResolver(TenantResolverProperties tenantResolverProperties) {

            return new FixedTenantResolver(tenantResolverProperties);
        }

    }

    @Configuration
    @Conditional(ResolverPropertiesTypeConfigurationCondition.class)
    public static class PropertiesTenantResolverConfiguration {


        @Bean
        public TenantResolver tenantResolver(TenantResolverProperties tenantResolverProperties) {

            return new SystemPropertiesTenantResolver(tenantResolverProperties);
        }
    }


    static class ResolverWebTypeConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ResolverEnvironmentPropertyUtils.tenantResolverType(context.getEnvironment(),
                    "type", "web");
        }
    }

    static class ResolverFixedTypeConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ResolverEnvironmentPropertyUtils.tenantResolverType(context.getEnvironment(),
                    "type", "fixed");
        }
    }

    static class ResolverPropertiesTypeConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ResolverEnvironmentPropertyUtils.tenantResolverType(context.getEnvironment(),
                    "type", "properties");
        }
    }


    //http resolver condition

    static class CookieConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ResolverEnvironmentPropertyUtils.tenantHttpWebResolverType(context.getEnvironment(),
                    "web-type", "cookie");
        }
    }

    static class HeaderConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ResolverEnvironmentPropertyUtils.tenantHttpWebResolverType(context.getEnvironment(),
                    "web-type", "header");
        }
    }

    static class SubDomainConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ResolverEnvironmentPropertyUtils.tenantHttpWebResolverType(context.getEnvironment(),
                    "web-type", "subdomain");
        }
    }

    static class ReqeustPathConfigurationCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ResolverEnvironmentPropertyUtils.tenantHttpWebResolverType(context.getEnvironment(),
                    "web-type", "path");
        }
    }
}
