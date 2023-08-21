package cc.sofast.infrastructure.customization.script.groovy;

import cc.sofast.infrastructure.customization.Cons;
import groovy.lang.Binding;
import groovy.lang.Script;
import org.springframework.context.ApplicationContext;

/**
 * @author apple
 */
public abstract class ScriptTemplate extends Script {
    @Override
    public Object run() {
        String tenant = getTenant();
        String key = getKey();
        ApplicationContext context = getApplicationContext();
        Binding binding = getBinding();
        return toRun(context, tenant, key, binding);
    }

    private ApplicationContext getApplicationContext() {
        return (ApplicationContext) getBinding().getVariable(Cons.APPLICATION_CONTEXT);
    }


    public String getTenant() {

        return String.valueOf(getBinding().getVariable(Cons.TENANT));
    }

    public String getKey() {

        return String.valueOf(getBinding().getVariable(Cons.KEY));
    }

    /**
     * 自定义run方法
     *
     * @param applicationContext {@link ApplicationContext}
     * @param tenant             String
     * @param key                String
     * @param binding            {@link  Binding}
     * @return Object
     */
    public abstract Object toRun(ApplicationContext applicationContext, String tenant, String key, Binding binding);
}
