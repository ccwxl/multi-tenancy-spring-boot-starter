package cc.sofast.infrastructure.func;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * function Configuration
 *
 * @author xielong.wang
 */
@Configuration
public class TenantBizFuncConfiguration {

    @Bean
    public TenantBizFuncExecutor tenantBizFuncExecutor() {

        return new TenantBizFuncExecutor();
    }
}
