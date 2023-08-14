package cc.sofast.infrastructure.customization;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xielong.wang
 * 个性化脚本自动配置
 */
@Configuration
public class TenantDynamicScriptExecutorConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TenantDynamicScriptExecutor tenantDynamicScriptExecutor(TenantCustomization tenantCustomization,
                                                                   ScriptExecutor scriptExecutor) {
        TenantDynamicScriptExecutor tdse = new TenantDynamicScriptExecutor();
        tdse.setTenantCustomization(tenantCustomization);
        tdse.setScriptExecutor(scriptExecutor);
        return tdse;
    }

    @Bean
    @ConditionalOnMissingBean
    public ScriptExecutor scriptExecutor() {

        return new ScriptExecutor();
    }

    @Bean
    public ApplicationContextHelper applicationContextHelper() {

        return new ApplicationContextHelper();
    }
}
