package cc.sofast.infrastructure.tenant.datasource;

import cc.sofast.infrastructure.tenant.datasource.creator.DataSourceCreator;
import cc.sofast.infrastructure.tenant.datasource.creator.DefaultDataSourceCreator;
import cc.sofast.infrastructure.tenant.datasource.creator.HikariDataSourceCreator;
import cc.sofast.infrastructure.tenant.datasource.creator.JndiDataSourceCreator;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @author apple
 */
@Configuration
public class DataSourceCreatorAutoconfiguration {

    public static final int JNDI_ORDER = 1000;
    public static final int HIKARI_ORDER = 2000;
    public static final int DRUID_ORDER = 3000;
    public static final int BEECP_ORDER = 4000;
    public static final int DBCP2_ORDER = 5000;
    public static final int DEFAULT_ORDER = 6000;

    @Primary
    @Bean
    @ConditionalOnMissingBean
    public DefaultDataSourceCreator dataSourceCreator(List<DataSourceCreator> dataSourceCreators) {

        return new DefaultDataSourceCreator(dataSourceCreators);
    }


    @Bean
    @Order(JNDI_ORDER)
    public JndiDataSourceCreator jndiDataSourceCreator() {
        return new JndiDataSourceCreator();
    }

    /**
     * 存在Hikari数据源时, 加入创建器
     */
    @ConditionalOnClass(HikariDataSource.class)
    @Configuration
    static class HikariDataSourceCreatorConfiguration {
        @Bean
        @Order(HIKARI_ORDER)
        public HikariDataSourceCreator hikariDataSourceCreator() {
            return new HikariDataSourceCreator();
        }
    }

}
