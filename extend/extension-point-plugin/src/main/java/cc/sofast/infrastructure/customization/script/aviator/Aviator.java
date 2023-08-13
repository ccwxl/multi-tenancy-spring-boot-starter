package cc.sofast.infrastructure.customization.script.aviator;

import cc.sofast.infrastructure.customization.script.EngineExecutorResult;
import cc.sofast.infrastructure.customization.script.Script;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

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
