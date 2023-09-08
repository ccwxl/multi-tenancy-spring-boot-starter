package cc.sofast.infrastructure.func;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author wxl
 */
public class TenantFunctionInvocationWrapper implements Function<Object, Object>, Consumer<Object>, Supplier<Object>, Runnable {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Object target;

    private Type inputType;

    private final Type outputType;

    private String functionDefinition;

    private boolean isSingleton = true;

    private boolean wrappedBiConsumer;

    TenantFunctionInvocationWrapper(String functionDefinition, Object target, Type inputType, Type outputType) {
        this.target = target;
        this.inputType = this.normalizeType(inputType);
        this.outputType = this.normalizeType(outputType);
        this.functionDefinition = functionDefinition;
    }

    /**
     * Will return Object.class if type is represented as TypeVariable(T) or WildcardType(?).
     */
    private Type normalizeType(Type type) {
        if (type != null) {
            return !(type instanceof TypeVariable) && !(type instanceof WildcardType) ? type : Object.class;
        }
        return type;
    }

    @Override
    public void run() {
        this.apply(null);
    }

    @Override
    public void accept(Object o) {
        this.apply(null);
    }

    @Override
    public Object apply(Object input) {
        if (logger.isDebugEnabled()) {
            logger.debug("Invoking function " + this);
        }

        return this.doApply(input);
    }

    private Object doApply(Object input) {
        Object result;
        if (this.isSupplier()) {
            result = ((Supplier) this.target).get();
        } else if (this.isConsumer()) {
            result = this.invokeConsumer(input);
        } else { // Function
            result = this.invokeFunction(input);
        }
        return result;
    }

    private Object invokeFunction(Object inputValue) {
        return ((Function) this.target).apply(inputValue);
    }

    private Object invokeConsumer(Object input) {
        ((Consumer) this.target).accept(null);
        return null;
    }

    @Override
    public Object get() {
        return this.apply(null);
    }


    public Object getTarget() {
        return target;
    }


    public String getFunctionDefinition() {
        return functionDefinition;
    }


    public Type getInputType() {
        return inputType;
    }

    public Type getOutputType() {
        return outputType;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean singleton) {
        isSingleton = singleton;
    }

    public void setWrappedBiConsumer(boolean wrappedBiConsumer) {
        this.wrappedBiConsumer = wrappedBiConsumer;
    }

    public boolean isConsumer() {
        return this.outputType == null;
    }

    public boolean isSupplier() {
        return this.inputType == null;
    }

    public boolean isFunction() {
        return this.inputType != null && this.outputType != null;
    }
}

