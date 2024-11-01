package cc.sofast.infrastructure.tenant.autoconfigure;

import cc.sofast.infrastructure.tenant.TenantBizExecutor;
import cc.sofast.infrastructure.tenant.TenantHelper;
import cc.sofast.infrastructure.tenant.datasource.DataSourceCreatorAutoconfiguration;
import cc.sofast.infrastructure.tenant.datasource.TenantDataSourceProperties;
import cc.sofast.infrastructure.tenant.datasource.TenantDataSourcePropertiesCustomizer;
import cc.sofast.infrastructure.tenant.datasource.TenantDynamicRoutingDataSource;
import cc.sofast.infrastructure.tenant.datasource.creator.DefaultDataSourceCreator;
import cc.sofast.infrastructure.tenant.datasource.provider.PropertiesTenantDataSourceProvider;
import cc.sofast.infrastructure.tenant.datasource.provider.TenantDataSourceProvider;
import cc.sofast.infrastructure.tenant.notify.TenancyNotifyAutoConfiguration;
import cc.sofast.infrastructure.tenant.propagation.PropagationConfiguration;
import cc.sofast.infrastructure.tenant.redis.TenantRedisConfiguration;
import cc.sofast.infrastructure.tenant.resolver.TenantResolverConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author apple
 */
@AutoConfiguration
@EnableConfigurationProperties(TenantDataSourceProperties.class)
@AutoConfigureBefore(value = DataSourceAutoConfiguration.class)
@Import({DataSourceCreatorAutoconfiguration.class,
        TenantResolverConfiguration.class, PropagationConfiguration.class,
        TenantRedisConfiguration.class, TenancyNotifyAutoConfiguration.class})
@ConditionalOnProperty(prefix = TenantDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class TenantAutoconfiguration implements InitializingBean {

    private final TenantDataSourceProperties properties;

    private final List<TenantDataSourcePropertiesCustomizer> dataSourcePropertiesCustomizers;

    public TenantAutoconfiguration(
            TenantDataSourceProperties properties,
            ObjectProvider<List<TenantDataSourcePropertiesCustomizer>> dataSourcePropertiesCustomizers) {
        this.properties = properties;
        this.dataSourcePropertiesCustomizers = dataSourcePropertiesCustomizers.getIfAvailable();
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(List<TenantDataSourceProvider> providers, DefaultDataSourceCreator defaultDataSourceCreator) {

        return new TenantDynamicRoutingDataSource(providers, defaultDataSourceCreator);
    }

    @Bean
    @ConditionalOnMissingBean
    public PropertiesTenantDataSourceProvider propertiesTenantDataSourceProvider(TenantDataSourceProperties tenantDataSourceProperties) {

        return new PropertiesTenantDataSourceProvider(tenantDataSourceProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantBizExecutor tenantBizExecutor(ApplicationContext context) {

        return new TenantBizExecutor(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public TenantHelper tenantHelper(DataSource dataSource) {

        return new TenantHelper((TenantDynamicRoutingDataSource) dataSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!CollectionUtils.isEmpty(dataSourcePropertiesCustomizers)) {
            for (TenantDataSourcePropertiesCustomizer customizer : dataSourcePropertiesCustomizers) {
                customizer.customize(properties);
            }
        }
    }
}
