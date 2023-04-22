package cc.sofast.infrastructure.tenant.datasource.provider;

import cc.sofast.infrastructure.tenant.datasource.DataSourceProperty;
import cc.sofast.infrastructure.tenant.datasource.DsType;
import cc.sofast.infrastructure.tenant.datasource.SeataMode;
import cc.sofast.infrastructure.tenant.datasource.TenantDataSourceProxy;
import cc.sofast.infrastructure.tenant.datasource.creator.AbstractDataSourceCreator;
import cc.sofast.infrastructure.tenant.datasource.creator.DefaultDataSourceCreator;
import com.p6spy.engine.spy.P6DataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.rm.datasource.xa.DataSourceProxyXA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author apple
 */
public abstract class AbstractTenantDataSourceProvider implements TenantDataSourceProvider {

    public static final String PREFIX = "#";

    private static final Logger log = LoggerFactory.getLogger(AbstractDataSourceCreator.class);

    private DefaultDataSourceCreator defaultDataSourceCreator;

    protected Map<String, DataSource> createDataSourceMap(
            Map<String, DataSourceProperty> dataSourcePropertiesMap) {
        Map<String, DataSource> dataSourceMap = new HashMap<>(dataSourcePropertiesMap.size() * 2);
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            String dsName = item.getKey();
            DataSourceProperty dataSourceProperty = item.getValue();
            String poolName = dataSourceProperty.getPoolName();
            if (poolName == null || "".equals(poolName)) {
                poolName = dsName;
            }
            dataSourceProperty.setPoolName(poolName);
            DataSource realDataSource = defaultDataSourceCreator.createDataSource(dataSourceProperty);
            //租户共享数据源
            if (dataSourceProperty.getTenantDsType().equals(DsType.SHARE)) {
                dataSourceMap.put(PREFIX + dsName, wrapDataSource(realDataSource, dataSourceProperty, item.getKey()));
                List<String> tenants = loadTenants(item.getKey());
                for (String tenant : tenants) {
                    dataSourceMap.put(tenant, wrapDataSource(realDataSource, dataSourceProperty, tenant));
                }
            } else {
                dataSourceMap.put(dsName, wrapDataSource(realDataSource, dataSourceProperty, item.getKey()));
            }
        }
        return dataSourceMap;
    }


    public static DataSource wrapDataSource(DataSource dataSource, DataSourceProperty dataSourceProperty, String tenant) {
        String name = dataSourceProperty.getPoolName();
        DataSource targetDataSource = dataSource;

        if (dataSourceProperty.getP6spy()) {
            targetDataSource = new P6DataSource(dataSource);
            log.debug("tenant-datasource [{}] wrap p6spy plugin", name);
        }

        SeataMode seataMode = dataSourceProperty.getSeataMode();
        if (dataSourceProperty.getSeata()) {
            if (SeataMode.XA == seataMode) {
                targetDataSource = new DataSourceProxyXA(targetDataSource);
            } else {
                targetDataSource = new DataSourceProxy(targetDataSource);
            }
            log.debug("tenant-datasource [{}] wrap seata plugin transaction mode ", name);
        }
        return new TenantDataSourceProxy(targetDataSource, dataSourceProperty.getTenantDsType().equals(DsType.SHARE), dataSourceProperty.getPoolName(), tenant, tenant);
    }

    public DefaultDataSourceCreator getDefaultDataSourceCreator() {
        return defaultDataSourceCreator;
    }

    public void setDefaultDataSourceCreator(DefaultDataSourceCreator defaultDataSourceCreator) {
        this.defaultDataSourceCreator = defaultDataSourceCreator;
    }
}
