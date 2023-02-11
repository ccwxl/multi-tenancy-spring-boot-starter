package cc.sofast.infrastructure.tenant.datasource.provider;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author apple
 */
public interface TenantDataSourceProvider {
    /**
     * 加载所有数据源
     *
     * @return 所有数据源，key为数据源名称
     */
    Map<String, DataSource> loadDataSources();


    /**
     * 加载共享数据源为key的租户列表
     *
     * @param ds 共享数据源key
     * @return 租户列表
     */
    List<String> loadTenants(String ds);
}
