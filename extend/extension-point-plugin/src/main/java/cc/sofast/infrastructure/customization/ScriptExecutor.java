package cc.sofast.infrastructure.customization;


import cc.sofast.infrastructure.customization.script.Script;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wxl
 * 脚本执行器，同步执行
 */
public class ScriptExecutor {

    Map<String, Script> scripts = new HashMap<>();

    public void init() {
        //扫描脚本执行器。进行注册。使用SPI的方式

    }

    public Object eval(DynamicScriptModel dsm, Map<String, Object> param) {

        return null;
    }
}
