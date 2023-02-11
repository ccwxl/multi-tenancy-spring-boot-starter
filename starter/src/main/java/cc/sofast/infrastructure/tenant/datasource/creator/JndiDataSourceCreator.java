package cc.sofast.infrastructure.tenant.datasource.creator;

import cc.sofast.infrastructure.tenant.datasource.DataSourceProperty;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

/**
 * JNDI数据源创建器
 *
 * @author TaoYu
 * @since 2020/1/27
 */
public class JndiDataSourceCreator extends AbstractDataSourceCreator implements DataSourceCreator {

    private static final JndiDataSourceLookup LOOKUP = new JndiDataSourceLookup();

    public DataSource createDataSource(String jndiName) {
        return LOOKUP.getDataSource(jndiName);
    }

    /**
     * 创建JNDI数据源
     *
     * @param dataSourceProperty jndi数据源名称
     * @return 数据源
     */
    @Override
    public DataSource doCreateDataSource(DataSourceProperty dataSourceProperty) {
        return createDataSource(dataSourceProperty.getJndiName());
    }

    @Override
    public boolean support(DataSourceProperty dataSourceProperty) {
        String jndiName = dataSourceProperty.getJndiName();
        return jndiName != null && !jndiName.isEmpty();
    }
}
