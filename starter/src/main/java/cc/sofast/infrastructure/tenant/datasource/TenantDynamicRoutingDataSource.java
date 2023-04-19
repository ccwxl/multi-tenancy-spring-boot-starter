package cc.sofast.infrastructure.tenant.datasource;

import cc.sofast.infrastructure.tenant.TenantContextHolder;
import cc.sofast.infrastructure.tenant.datasource.provider.TenantDataSourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author apple
 */
public class TenantDynamicRoutingDataSource extends AbstractDataSource
        implements TenantDataSourceRegister, InitializingBean, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(TenantDynamicRoutingDataSource.class);

    private static final String DEF = "def";

    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    private final List<TenantDataSourceProvider> tenantDataSourceProvider;

    public TenantDynamicRoutingDataSource(List<TenantDataSourceProvider> tenantDataSourceProvider) {
        this.tenantDataSourceProvider = tenantDataSourceProvider;
    }

    @Override
    public Connection getConnection() throws SQLException {
        DataSource dataSource = determineDataSource();
        return dataSource.getConnection();
    }

    private DataSource determineDataSource() {
        String dsKey = TenantContextHolder.peek();
        return getDataSource(dsKey);
    }

    /**
     * 获取数据源
     *
     * @param ds 数据源名称
     * @return 数据源
     */
    public DataSource getDataSource(String ds) {
        if (!StringUtils.hasLength(ds)) {
            return determineDefDataSource();
        }
        return dataSourceMap.get(ds);
    }

    /**
     * 获取默认数据源
     *
     * @return def 数据源
     */
    private DataSource determineDefDataSource() {

        return dataSourceMap.get(DEF);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        DataSource dataSource = determineDataSource();
        return dataSource.getConnection(username, password);
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化所有数据
        Map<String, DataSource> dataSources = new HashMap<>(16);
        for (TenantDataSourceProvider provider : tenantDataSourceProvider) {
            dataSources.putAll(provider.loadDataSources());
        }

        //添加到数据源中
        for (Map.Entry<String, DataSource> dsItem : dataSources.entrySet()) {
            addDataSource(dsItem.getKey(), dsItem.getValue());
        }
    }


    /**
     * 添加数据源
     *
     * @param ds         数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(String ds, DataSource dataSource) {
        DataSource oldDataSource = dataSourceMap.put(ds, dataSource);
        // 关闭老的数据源
        if (oldDataSource != null) {
            closeDataSource(ds, oldDataSource);
        }
        log.info("tenant-datasource - add a datasource named [{}] success", ds);
    }


    /**
     * close db
     *
     * @param ds         dsName
     * @param dataSource db
     */
    private void closeDataSource(String ds, DataSource dataSource) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        return determineDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return (iface.isInstance(this) || determineDataSource().isWrapperFor(iface));
    }

    @Override
    public boolean register(String tenant, DataSourceProperty dsp) {

        return false;
    }

    @Override
    public boolean remove(String tenant) {


        return false;
    }
}
