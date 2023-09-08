package cc.sofast.infrastructure.func;



import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author xielong.wang
 */
public abstract class FunctionContextUtils {

    public static Type findType(ConfigurableListableBeanFactory registry, String... names) {
        AbstractBeanDefinition definition = null;
        String actualName = null;
        for (String name : names) {
            if (registry.containsBeanDefinition(name)) {
                definition = (AbstractBeanDefinition) registry.getBeanDefinition(name);
                actualName = name;
            } else if (registry.containsBean(name)) {
                return FunctionTypeUtils.discoverFunctionTypeFromClass(registry.getBean(name).getClass());
            }
        }

        Class<?> beanClass;


        if (definition == null) {
            return null;
        } else {
            beanClass = resolveBeanClass(definition);
        }

        Object source = definition.getSource();

        Type param = null;
        if (source instanceof MethodMetadata) {
            param = findBeanType(definition, ((MethodMetadata) source).getDeclaringClassName(), ((MethodMetadata) source).getMethodName());
        } else if (source instanceof Resource) {
            param = registry.getType(actualName);
        }

        if (param == null) {
            param = definition.getResolvableType().getType();
        }

        if (!(param instanceof ParameterizedType) && beanClass != null) {
            return FunctionTypeUtils.discoverFunctionTypeFromClass(beanClass);
        }
        return param;
    }

    public static Class<?>[] getParamTypesFromBeanDefinitionFactory(Class<?> factory,
                                                                    AbstractBeanDefinition definition) {
        if (definition instanceof RootBeanDefinition) {
            RootBeanDefinition root = (RootBeanDefinition) definition;
            for (Method method : getCandidateMethods(factory, root)) {
                if (root.isFactoryMethod(method)) {
                    return method.getParameterTypes();
                }
            }
        }
        List<Class<?>> params = new ArrayList<>();
        for (ConstructorArgumentValues.ValueHolder holder : definition
                .getConstructorArgumentValues().getIndexedArgumentValues().values()) {
            params.add(ClassUtils.resolveClassName(holder.getType(), null));
        }
        return params.toArray(new Class<?>[0]);
    }

    private static Class<?> resolveBeanClass(AbstractBeanDefinition beanDefinition) {
        try {
            return beanDefinition.hasBeanClass() ? beanDefinition.getBeanClass() : ClassUtils.getDefaultClassLoader().loadClass(beanDefinition.getBeanClassName());
        } catch (Exception e) {
            return null;
        }
    }

    private static Type findBeanType(AbstractBeanDefinition definition, String declaringClassName, String methodName) {
        Class<?> factory = ClassUtils.resolveClassName(declaringClassName, null);
        Class<?>[] params = getParamTypesFromBeanDefinitionFactory(factory, definition);
        Method method = ReflectionUtils.findMethod(factory, methodName,
                params);
        Type type = method.getGenericReturnType();
        return type;
    }

    private static Method[] getCandidateMethods(final Class<?> factoryClass,
                                                final RootBeanDefinition mbd) {
        return (mbd.isNonPublicAccessAllowed()
                ? ReflectionUtils.getAllDeclaredMethods(factoryClass)
                : factoryClass.getMethods());
    }
}


