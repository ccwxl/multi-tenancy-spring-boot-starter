package cc.sofast.infrastructure.tenant.datasource.provider;

import cc.sofast.infrastructure.tenant.datasource.creator.DefaultDataSourceCreator;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author apple
 */
public class JdbcTenantDataSourceProvider extends AbstractTenantDataSourceProvider {


    public JdbcTenantDataSourceProvider() {

    }

    @Override
    public Map<String, DataSource> loadDataSources() {
        return null;
    }

    @Override
    public List<String> loadTenants(String ds) {
        return null;
    }
}
