package cc.sofast.infrastructure.customization.script;

import com.google.auto.service.AutoService;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.script.AviatorScriptEngine;

import java.util.Map;

/**
 * @author wxl
 */
@AutoService(Script.class)
public class Aviator implements Script {


    @Override
    public String type() {
        return "aviator";
    }

    @Override
    public EngineExecutorResult eval(String script, Map<String, Object> param) {
        Expression compile = AviatorEvaluator.compile(script, true);
        return EngineExecutorResult.success(compile.execute(param));
    }
}
