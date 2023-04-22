package cc.sofast.infrastructure.tenant.datasource.provider;

import cc.sofast.infrastructure.tenant.datasource.DataSourceProperty;
import cc.sofast.infrastructure.tenant.datasource.TenantDataSourceProperties;
import cc.sofast.infrastructure.tenant.datasource.creator.DefaultDataSourceCreator;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author apple
 */
public class PropertiesTenantDataSourceProvider extends AbstractTenantDataSourceProvider {

    private final TenantDataSourceProperties tenantDataSourceProperties;

    public PropertiesTenantDataSourceProvider(TenantDataSourceProperties tenantDataSourceProperties) {
        this.tenantDataSourceProperties = tenantDataSourceProperties;
    }

    @Override
    public Map<String, DataSource> loadDataSources() {

        return createDataSourceMap(tenantDataSourceProperties.getDatasource());
    }

    @Override
    public List<String> loadTenants(String ds) {
        Map<String, DataSourceProperty> datasource = tenantDataSourceProperties.getDatasource();
        DataSourceProperty dataSourceProperty = datasource.get(ds);
        return dataSourceProperty.getTenants();
    }
}
