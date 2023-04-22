package cc.sofast.infrastructure.tenant.datasource;

/**
 * @author apple
 */
public interface TenantDataSourceRegister {

    boolean register(DataSourceProperty dsp);

    boolean remove(String tenant);
}
