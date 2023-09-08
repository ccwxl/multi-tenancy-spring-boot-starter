package cc.sofast.infrastructure.func;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author wxl
 */
public class TenantBizFuncExecutor implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(TenantBizFuncExecutor.class);

    private GenericApplicationContext applicationContext;

    private static final Map<TenantBizFun, String> TENANT_MAPPING = new ConcurrentHashMap<>();

    private final Set<TenantFunctionRegistration<?>> functionRegistrations = new CopyOnWriteArraySet<>();

    private final Map<String, TenantFunctionInvocationWrapper> wrappedFunctionDefinitions = new HashMap<>();

    public <T> T lookup(TenantBizFun tenantBizFun) {
        //获取所有标注TenantFun的注解的bean
        String funName = TENANT_MAPPING.get(tenantBizFun);
        if (!StringUtils.hasText(funName)) {
            //TODO if funName real is null？
            Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(TenantFun.class);
            for (Map.Entry<String, Object> b : beansWithAnnotation.entrySet()) {
                BeanDefinition bd = applicationContext.getBeanDefinition(b.getKey());
                MergedAnnotations mergedAnnotations = null;
                if (bd instanceof ScannedGenericBeanDefinition) {
                    AnnotationMetadata metadata = ((ScannedGenericBeanDefinition) bd).getMetadata();
                    mergedAnnotations = metadata.getAnnotations();

                }

                if (bd instanceof RootBeanDefinition) {
                    if (bd.getSource() instanceof AnnotatedTypeMetadata metadata) {
                        mergedAnnotations = metadata.getAnnotations();
                    }
                }

                if (mergedAnnotations == null) {
                    log.warn("mergedAnnotations is null BeanDefinition is: [{}]", bd);
                    continue;
                }

                MergedAnnotation<TenantFun> tenantFunMergedAnnotation = mergedAnnotations.get(TenantFun.class);
                String tenant = tenantFunMergedAnnotation.getString("tenant");
                String biz = tenantFunMergedAnnotation.getString("biz");
                TENANT_MAPPING.put(TenantBizFun.of(tenant, biz), b.getKey());

            }
            funName = TENANT_MAPPING.get(tenantBizFun);
        }
        if (!StringUtils.hasText(funName)) {
            throw new IllegalArgumentException("function isnull");
        }

        return lookup(funName);
    }

    private <T> T lookup(String functionDefinition) {
        //TenantFunctionInvocationWrapper 和funName缓存。
        TenantFunctionInvocationWrapper function = this.wrappedFunctionDefinitions.get(functionDefinition);
        Object syncInstance = functionDefinition == null ? this : functionDefinition;
        synchronized (syncInstance) {
            if (function == null) {
                Object functionCandidate = discoverFunctionInBeanFactory(functionDefinition);
                TenantFunctionRegistration functionRegistration = null;
                if (functionCandidate != null) {
                    Type functionType = FunctionTypeUtils.discoverFunctionType(functionCandidate, functionDefinition, this.applicationContext);
                    if (functionRegistration == null) {
                        functionRegistration = new TenantFunctionRegistration(functionCandidate, functionDefinition).type(functionType);
                    }
                    this.register(functionRegistration);
                }
            }
            function = doLookups(functionDefinition);
        }
        return (T) function;
    }

    <T> T doLookups(String functionDefinition) {
        TenantFunctionInvocationWrapper function = this.wrappedFunctionDefinitions.get(functionDefinition);
        if (function == null) {
            function = compose(functionDefinition);
        }
        return (T) function;
    }

    private TenantFunctionInvocationWrapper compose(String functionDefinition) {
        TenantFunctionInvocationWrapper composedFunction = null;

        TenantFunctionInvocationWrapper function = this.findFunctionInFunctionRegistrations(functionDefinition);
        if (function == null) {
            return null;
        } else {
            if (composedFunction == null) {
                composedFunction = function;
            } else {
                TenantFunctionInvocationWrapper andThenFunction =
                        invocationWrapperInstance(functionDefinition, function.getTarget(), function.getInputType(), function.getOutputType());
                composedFunction = (TenantFunctionInvocationWrapper) composedFunction.andThen((Function<Object, Object>) andThenFunction);
            }
            if (composedFunction.isSingleton()) {
                this.wrappedFunctionDefinitions.put(composedFunction.getFunctionDefinition(), composedFunction);
            }
        }

        return composedFunction;
    }

    private TenantFunctionInvocationWrapper invocationWrapperInstance(String functionDefinition, Object target, Type inputType, Type outputType) {
        return new TenantFunctionInvocationWrapper(functionDefinition, target, inputType, outputType);
    }

    private TenantFunctionInvocationWrapper invocationWrapperInstance(String functionDefinition, Object target, Type functionType) {
        return invocationWrapperInstance(functionDefinition, target,
                FunctionTypeUtils.isSupplier(functionType) ? null : FunctionTypeUtils.getInputType(functionType),
                FunctionTypeUtils.getOutputType(functionType));
    }


    private TenantFunctionInvocationWrapper findFunctionInFunctionRegistrations(String functionName) {
        TenantFunctionRegistration<?> functionRegistration = this.functionRegistrations.stream()
                .filter(fr -> fr.getNames().contains(functionName))
                .findFirst()
                .orElseGet(() -> null);
        TenantFunctionInvocationWrapper function = functionRegistration != null
                ? this.invocationWrapperInstance(functionName, functionRegistration.getTarget(), functionRegistration.getType())
                : null;
        if (functionRegistration != null) {
            Object userFunction = functionRegistration.getUserFunction();
            if (userFunction instanceof BiConsumer && function != null) {
                function.setWrappedBiConsumer(true);
            }
        }
        if (functionRegistration != null && functionRegistration.getProperties().containsKey("singleton")) {
            try {
                function.setSingleton(Boolean.parseBoolean(functionRegistration.getProperties().get("singleton")));
            } catch (Exception e) {
                // ignore
            }
        }
        return function;
    }

    private void register(TenantFunctionRegistration registration) {
        this.functionRegistrations.add(registration);
    }


    private Object discoverFunctionInBeanFactory(String functionName) {
        Object functionCandidate = null;
        if (this.applicationContext.containsBean(functionName)) {
            functionCandidate = this.applicationContext.getBean(functionName);
        } else {
            try {
                functionCandidate = BeanFactoryAnnotationUtils.qualifiedBeanOfType(this.applicationContext.getBeanFactory(), Object.class, functionName);
            } catch (Exception e) {
                // ignore since there is no safe isAvailable-kind of method
            }
        }
        return functionCandidate;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (GenericApplicationContext) applicationContext;
    }
}
