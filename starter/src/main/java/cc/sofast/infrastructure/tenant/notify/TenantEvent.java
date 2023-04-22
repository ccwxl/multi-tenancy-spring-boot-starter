package cc.sofast.infrastructure.tenant.notify;

import cc.sofast.infrastructure.tenant.datasource.DsType;
import cc.sofast.infrastructure.tenant.datasource.SeataMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author apple
 */
public class TenantEvent {

    /**
     * 连接池名称(只是一个名称标识)</br> 默认是配置文件上的名称
     */
    private String poolName;

    /**
     * 操作类型
     */
    private TenantEventType type;

    /**
     * 租户列表
     */
    private List<String> tenants = new ArrayList<>();

    /**
     * 数据源类型
     */
    private DsType tenantDsType = DsType.EXCLUSIVE;

    /**
     * 时间戳
     */
    private Long eventTs;

    /**
     * 是否启用seata
     */
    private Boolean seata = false;

    /**
     * seata使用模式，默认AT
     */
    private SeataMode seataMode = SeataMode.AT;

    /**
     * 连接池类型，如果不设置自动查找 Druid > HikariCp
     */
    private String dsType;

    /**
     * JDBC driver
     */
    private String driverClassName;

    /**
     * JDBC url 地址
     */
    private String url;

    /**
     * JDBC 用户名
     */
    private String username;

    /**
     * JDBC 密码
     */
    private String password;

    /**
     * 最大存活的连接数
     */
    private Integer maxPoolSize;

    /**
     * 最小存活的连接数
     */
    private Integer minIdle;

    private Boolean p6spy = false;

    public TenantEvent() {
        this.eventTs = System.currentTimeMillis();
    }

    public TenantEventType getType() {
        return type;
    }

    public void setType(TenantEventType type) {
        this.type = type;
    }

    public List<String> getTenants() {
        return tenants;
    }

    public void setTenants(List<String> tenants) {
        this.tenants = tenants;
    }

    public DsType getTenantDsType() {
        return tenantDsType;
    }

    public void setTenantDsType(DsType tenantDsType) {
        this.tenantDsType = tenantDsType;
    }

    public Boolean getSeata() {
        return seata;
    }

    public void setSeata(Boolean seata) {
        this.seata = seata;
    }

    public SeataMode getSeataMode() {
        return seataMode;
    }

    public void setSeataMode(SeataMode seataMode) {
        this.seataMode = seataMode;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getDsType() {
        return dsType;
    }

    public void setDsType(String dsType) {
        this.dsType = dsType;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Long getEventTs() {
        return eventTs;
    }

    public void setEventTs(Long eventTs) {
        this.eventTs = eventTs;
    }

    public Boolean getP6spy() {
        return p6spy;
    }

    public void setP6spy(Boolean p6spy) {
        this.p6spy = p6spy;
    }
}
