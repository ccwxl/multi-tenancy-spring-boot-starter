package cc.sofast.infrastructure.tenant.autoconfigure;

import cc.sofast.infrastructure.tenant.datasource.DataSourceCreatorAutoconfiguration;
import cc.sofast.infrastructure.tenant.datasource.TenantDataSourceProperties;
import cc.sofast.infrastructure.tenant.datasource.TenantDataSourcePropertiesCustomizer;
import cc.sofast.infrastructure.tenant.datasource.TenantDynamicRoutingDataSource;
import cc.sofast.infrastructure.tenant.datasource.creator.DefaultDataSourceCreator;
import cc.sofast.infrastructure.tenant.datasource.provider.PropertiesTenantDataSourceProvider;
import cc.sofast.infrastructure.tenant.datasource.provider.TenantDataSourceProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author apple
 */
@Configuration
@EnableConfigurationProperties(TenantDataSourceProperties.class)
@AutoConfigureBefore(value = DataSourceAutoConfiguration.class)
@Import(DataSourceCreatorAutoconfiguration.class)
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
    public DataSource dataSource(List<TenantDataSourceProvider> providers) {

        return new TenantDynamicRoutingDataSource(providers);
    }

    @Bean
    @ConditionalOnMissingBean
    public PropertiesTenantDataSourceProvider propertiesTenantDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
                                                                                 TenantDataSourceProperties tenantDataSourceProperties) {

        return new PropertiesTenantDataSourceProvider(defaultDataSourceCreator, tenantDataSourceProperties);
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
