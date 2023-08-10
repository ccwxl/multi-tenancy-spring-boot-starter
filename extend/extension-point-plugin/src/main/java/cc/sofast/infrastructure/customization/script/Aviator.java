package cc.sofast.infrastructure.customization.script;

import com.google.auto.service.AutoService;

import java.util.Map;

/**
 * @author wxl
 */
@AutoService(Script.class)
public class Aviator implements Script {

    @Override
    public void init() {
        //脚本的缓存，初始化等等

    }

    @Override
    public Object eval(String script, Map<String, Object> param) {

        return null;
    }


    @Override
    public void destroy() {

    }
}
