package cc.sofast.infrastructure.tenant;

import cc.sofast.infrastructure.tenant.datasource.TenantDynamicRoutingDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author apple
 * 获取所有租户列表
 * 获取当前的租户
 */
public class TenantHelper {

    public final TenantDynamicRoutingDataSource tenantDynamicRoutingDataSource;

    public TenantHelper(TenantDynamicRoutingDataSource tenantDynamicRoutingDataSource) {
        this.tenantDynamicRoutingDataSource = tenantDynamicRoutingDataSource;
    }

    /**
     * 1. # 号开头的不是真正的租户
     * 2. def 不是真正的租户
     * 3. 增加一个谓词匹配，如果不匹配也不是真正的租户
     *
     * @param predicate Predicate<String>
     * @return tenant
     */
    public List<String> getTenantList(Predicate<String> predicate) {
        Set<String> dsKeys = tenantDynamicRoutingDataSource.getDataSourceMap().keySet();
        List<String> tenants = new ArrayList<>();
        for (String dsKey : dsKeys) {
            if (!dsKey.startsWith("#") && "def".equals(dsKey) && predicate.test(dsKey)) {
                tenants.add(dsKey);
            }
        }
        return tenants;
    }
}
