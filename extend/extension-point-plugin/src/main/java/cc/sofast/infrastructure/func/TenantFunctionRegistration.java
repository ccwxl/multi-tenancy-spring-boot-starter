package cc.sofast.infrastructure.func;


import org.springframework.beans.factory.BeanNameAware;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author wxl
 */
public class TenantFunctionRegistration<T> implements BeanNameAware {
    /**
     * Suffix used to add to the name of FunctionRegistration bean that
     * corresponds to the an actual function bean. It is often used when
     * the actual function bean may not be a java Function (e.g., Kotlin)
     * and certain custom wrapping is required.
     * <br>
     * NOTE: This is not intended as oublis API
     */
    public static String REGISTRATION_NAME_SUFFIX = "_registration";

    private final Set<String> names = new LinkedHashSet<>();

    private final Map<String, String> properties = new LinkedHashMap<>();

    private T target;

    private Type type;

    private Object userFunction;

    public TenantFunctionRegistration(T target, String... names) {
        Assert.notNull(target, "'target' must not be null");
        this.target = target;
        this.names(names);
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public Set<String> getNames() {
        return this.names;
    }

    /**
     * Will set the names for this registration clearing all previous names first. If you
     * want to add a name or set or names to the existing set of names use
     * {@link #names(Collection)} or {@link #name(String)} or {@link #names(String...)}
     * operations.
     * @param names - bean names
     */
    public void setNames(Set<String> names) {
        this.names.clear();
        this.names.addAll(names);
    }

    public Type getType() {
        return this.type;
    }

    public T getTarget() {
        return this.target;
    }

    public TenantFunctionRegistration<T> properties(Map<String, String> properties) {
        this.properties.putAll(properties);
        return this;
    }

    public TenantFunctionRegistration<T> type(Type type) {
        this.type = type;

        Type discoveredFunctionType = FunctionTypeUtils.discoverFunctionTypeFromClass(this.target.getClass());
        if (discoveredFunctionType == null) { // only valid for Kafka Stream KStream[] return type.
            return null;
        }

        Class<?> inputType = FunctionTypeUtils.getRawType(FunctionTypeUtils.getInputType(discoveredFunctionType));
        Class<?> outputType = FunctionTypeUtils.getRawType(FunctionTypeUtils.getOutputType(discoveredFunctionType));

        if (inputType != null && inputType != Object.class && outputType != null && outputType != Object.class) {
            Assert.isTrue((inputType.isAssignableFrom(FunctionTypeUtils.getRawType(FunctionTypeUtils.getInputType(type)))
                            && outputType.isAssignableFrom(FunctionTypeUtils.getRawType(FunctionTypeUtils.getOutputType(type)))),
                    "Discovered function type does not match provided function type. Discovered: "
                            + discoveredFunctionType + "; Provided: " + type);
        }
        else if (inputType == null && outputType != Object.class) {
            Assert.isTrue(outputType.isAssignableFrom(FunctionTypeUtils.getRawType(FunctionTypeUtils.getOutputType(type))),
                    "Discovered function type does not match provided function type. Discovered: "
                            + discoveredFunctionType + "; Provided: " + type);
        }
        else if (outputType == null && inputType != Object.class) {
            Assert.isTrue(inputType.isAssignableFrom(FunctionTypeUtils.getRawType(FunctionTypeUtils.getInputType(type))),
                    "Discovered function type does not match provided function type. Discovered: "
                            + discoveredFunctionType + "; Provided: " + type);
        }


        return this;
    }

    /**
     * Allows to override the target of this registration with a new target that typically
     * wraps the original target. This typically happens when original target is wrapped
     * into its {@link flux} counterpart (e.g., Function into FluxFunction)
     * @param target new target
     * @return this registration with new target
     */
    public TenantFunctionRegistration<T> target(T target) {
        this.target = target;
        return this;
    }

    public TenantFunctionRegistration<T> name(String name) {
        return this.names(name);
    }

    public TenantFunctionRegistration<T> names(Collection<String> names) {
        this.names.addAll(names);
        return this;
    }

    public TenantFunctionRegistration<T> names(String... names) {
        return this.names(Arrays.asList(names));
    }

    /**
     * Transforms (wraps) function identified by the 'target' to its {@code Flux}
     * equivalent unless it already is. For example, {@code Function<String, String>}
     * becomes {@code Function<Flux<String>, Flux<String>>}
     * @param <S> the expected target type of the function (e.g., FluxFunction)
     * @return {@code FunctionRegistration} with the appropriately wrapped target.
     *
     */
    @Override
    public void setBeanName(String name) {
        if (CollectionUtils.isEmpty(this.names)) {
            this.name(name);
        }
    }

    public Object getUserFunction() {
        return userFunction;
    }

    public void setUserFunction(Object userFunction) {
        this.userFunction = userFunction;
    }
}

