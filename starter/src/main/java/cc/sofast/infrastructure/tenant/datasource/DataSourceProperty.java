package cc.sofast.infrastructure.tenant.datasource;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author apple
 */
public class DataSourceProperty {
    /**
     * 连接池名称(只是一个名称标识)</br> 默认是配置文件上的名称
     */
    private String poolName;
    /**
     * 连接池类型，如果不设置自动查找 Druid > HikariCp
     */
    private Class<? extends DataSource> type;
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
     * 是否启用seata
     */
    private Boolean seata = false;
    /**
     * seata使用模式，默认AT
     */
    private SeataMode seataMode = SeataMode.AT;

    /**
     * 是否启用p6spy
     */
    private Boolean p6spy = false;
    /**
     * lazy init datasource
     */
    private Boolean lazy;

    /**
     * 租户数据源类型
     */
    private DsType tenantDsType = DsType.EXCLUSIVE;

    /**
     * 租户列表
     */
    private List<String> tenants = new ArrayList<>();

    /**
     * HikariCp参数配置
     */
    @NestedConfigurationProperty
    private HikariConfig hikari = new HikariConfig();

    private String jndiName;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public Class<? extends DataSource> getType() {
        return type;
    }

    public void setType(Class<? extends DataSource> type) {
        this.type = type;
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

    public Boolean getSeata() {
        return seata;
    }

    public void setSeata(Boolean seata) {
        this.seata = seata;
    }

    public Boolean getP6spy() {
        return p6spy;
    }

    public void setP6spy(Boolean p6spy) {
        this.p6spy = p6spy;
    }

    public Boolean getLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }

    public HikariConfig getHikari() {
        return hikari;
    }

    public void setHikari(HikariConfig hikari) {
        this.hikari = hikari;
    }

    public List<String> getTenants() {
        return tenants;
    }

    public void setTenants(List<String> tenants) {
        this.tenants = tenants;
    }

    public SeataMode getSeataMode() {
        return seataMode;
    }

    public void setSeataMode(SeataMode seataMode) {
        this.seataMode = seataMode;
    }

    public String getJndiName() {

        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public DsType getTenantDsType() {
        return tenantDsType;
    }

    public void setTenantDsType(DsType tenantDsType) {
        this.tenantDsType = tenantDsType;
    }
}
