package cc.sofast.infrastructure.customization;

import cc.sofast.infrastructure.customization.db.StandardSQLCustomizationLoader;
import cc.sofast.infrastructure.customization.mem.MemCustomizationLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author xielong.wang
 * 个性化配置项加载
 */
@Configuration
public class TenantCustomizationConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TenantCustomization tenantCustomization(ComposeCustomizationLoader customizationLoader) {
        return new TenantCustomization(customizationLoader);
    }

    @Bean
    @ConditionalOnMissingBean
    public ComposeCustomizationLoader customizationLoader(MemCustomizationLoader memCustomizationLoader, PersistentCustomizationLoader persistentCustomizationLoader) {
        return new ComposeCustomizationLoader(memCustomizationLoader, persistentCustomizationLoader);
    }

    @Bean
    @ConditionalOnMissingBean
    public MemCustomizationLoader memCustomizationLoader(StringRedisTemplate stringRedisTemplate) {

        return new MemCustomizationLoader(stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public PersistentCustomizationLoader persistentCustomizationLoader(JdbcTemplate jdbcTemplate) {

        return new StandardSQLCustomizationLoader(jdbcTemplate);
    }

}
