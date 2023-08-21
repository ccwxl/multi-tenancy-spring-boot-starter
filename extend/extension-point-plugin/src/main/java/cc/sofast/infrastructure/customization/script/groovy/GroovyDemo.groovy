package cc.sofast.infrastructure.customization.script.groovy

import org.springframework.context.ApplicationContext

class GroovyDemo extends ScriptTemplate {

    @Override
    Object toRun(ApplicationContext applicationContext, String tenant, String key, Binding binding) {
        println("this is template. " + getBinding().getVariable("idx"))
        return "ok"
    }
}