package cc.sofast.infrastructure.tenant.datasource.creator;


import cc.sofast.infrastructure.tenant.datasource.DataSourceProperty;
import cc.sofast.infrastructure.tenant.datasource.TenantDataSourceProperties;

import javax.sql.DataSource;

/**
 * 抽象连接池创建器
 * <p>
 * 这里主要处理一些公共逻辑
 *
 * @author TaoYu
 */
public abstract class AbstractDataSourceCreator implements DataSourceCreator {

    protected TenantDataSourceProperties properties;

    /**
     * 子类去实际创建连接池
     *
     * @param dataSourceProperty 数据源信息
     * @return 实际连接池
     */
    public abstract DataSource doCreateDataSource(DataSourceProperty dataSourceProperty);

    @Override
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        Boolean lazy = dataSourceProperty.getLazy();
        if (lazy == null) {
            dataSourceProperty.setLazy(lazy);
        }
        return doCreateDataSource(dataSourceProperty);
    }
}
