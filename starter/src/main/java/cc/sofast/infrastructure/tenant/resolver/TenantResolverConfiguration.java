package cc.sofast.infrastructure.tenant.resolver;

import cc.sofast.infrastructure.tenant.resolver.http.CookieTenantResolver;
import cc.sofast.infrastructure.tenant.resolver.http.HeaderTenantResolver;
import cc.sofast.infrastructure.tenant.resolver.http.RequestPathTenantResolver;
import cc.sofast.infrastructure.tenant.resolver.http.SubdomainTenantResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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


    }

}
