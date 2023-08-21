package cc.sofast.infrastructure.customization;


import cc.sofast.infrastructure.customization.script.EngineExecutorResult;
import cc.sofast.infrastructure.customization.script.Script;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author wxl
 * 脚本执行器，同步执行
 */
public class ScriptExecutor {

    Map<String, Script> scripts = new HashMap<>();

    public ScriptExecutor() {
        init();
    }

    public void init() {
        //扫描脚本执行器。进行注册。使用SPI的方式
        ServiceLoader<Script> load = ServiceLoader.load(Script.class);
        for (Script script : load) {
            String type = script.type();
            scripts.put(type, script);
        }
    }

    public EngineExecutorResult eval(DynamicScriptModel dsm, Map<String, Object> param) {
        String type = dsm.getType();
        Script script = scripts.get(type);
        if (script == null) {
            throw new IllegalArgumentException("unknown script type: " + type + " support aviator,groovy");
        }
        return script.eval(dsm.getScript(), param);
    }
}
