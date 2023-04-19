package cc.sofast.infrastructure.tenant.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author apple
 */
@ConfigurationProperties(prefix = TenantDataSourceProperties.PREFIX)
public class TenantDataSourceProperties {

    public static final String PREFIX = "spring.multitenancy";

    /**
     * 必须设置默认的库,默认def
     */
    private String primary = "def";


    /**
     * 是否开启多租户模式
     */
    private boolean enabled;

    /**
     * 每一个数据源
     */
    private Map<String, DataSourceProperty> datasource = new LinkedHashMap<>();

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public Map<String, DataSourceProperty> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, DataSourceProperty> datasource) {
        this.datasource = datasource;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
