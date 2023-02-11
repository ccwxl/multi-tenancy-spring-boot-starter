package cc.sofast.infrastructure.tenant.datasource;

/**
 * @author apple
 */
public interface TenantDataSourcePropertiesCustomizer {

    /**
     * Customize the given a {@link TenantDataSourceProperties} object.
     *
     * @param properties the DynamicDataSourceProperties object to customize
     */
    void customize(TenantDataSourceProperties properties);
}
