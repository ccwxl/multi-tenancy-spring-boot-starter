package cc.sofast.infrastructure.tenant.datasource.creator;


import cc.sofast.infrastructure.tenant.datasource.DataSourceProperty;

import javax.sql.DataSource;
import java.util.List;

/**
 * 数据源创建器
 *
 * @author TaoYu
 * @since 2.3.0
 */
public class DefaultDataSourceCreator {

    private final List<DataSourceCreator> creators;

    public DefaultDataSourceCreator(List<DataSourceCreator> creators) {
        this.creators = creators;
    }

    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        DataSourceCreator dataSourceCreator = null;
        for (DataSourceCreator creator : this.creators) {
            if (creator.support(dataSourceProperty)) {
                dataSourceCreator = creator;
                break;
            }
        }
        if (dataSourceCreator == null) {
            throw new IllegalStateException("creator must not be null,please check the DataSourceCreator");
        }
        return dataSourceCreator.createDataSource(dataSourceProperty);
    }

}
