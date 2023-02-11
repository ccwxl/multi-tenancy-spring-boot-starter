package cc.sofast.infrastructure.tenant;

import cc.sofast.infrastructure.tenant.context.TenantContextHolder;
import org.springframework.context.ApplicationContext;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author apple
 *
 * <p>
 * 租户业务逻辑执行器.
 * 1. 传入tenant和业务执行逻辑.</pr>
 * 2. 自动切换租户的上下文.执行.清理工作.</pr>
 * </p>
 */
public class TenantBizExecutor {
    private final ApplicationContext applicationContext;

    public TenantBizExecutor(ApplicationContext applicationContext) {

        this.applicationContext = applicationContext;
    }

    public <R, T> R execute( String tenant, Class<T> targetClz,Function<T, R> exeFunction) {
        try {
            T bean = applicationContext.getBean(targetClz);
            TenantContextHolder.push(tenant);
            return exeFunction.apply(bean);
        } finally {
            TenantContextHolder.poll();
        }
    }

    public <T> void execute( String tenant, Class<T> targetClz,Consumer<T> exeFunction) {
        try {
            T bean = applicationContext.getBean(targetClz);
            TenantContextHolder.push(tenant);
            exeFunction.accept(bean);
        } finally {
            TenantContextHolder.poll();
        }
    }
}

