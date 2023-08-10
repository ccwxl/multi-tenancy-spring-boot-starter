package cc.sofast.infrastructure.customization.script;

import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wxl
 * groovy 脚本动态执行
 */
@AutoService(Script.class)
public class Groovy implements Script {

    private Map<String, ClassLoader> CLASS_CACHE = new HashMap<>();

    @Override
    public void init() {
        //Groovy 脚本引擎 初始化。classloader cache

    }

    @Override
    public Object eval(String script, Map<String, Object> param) {
        //计算下脚本的md5，从缓存执行。

        return null;
    }

    @Override
    public void destroy() {

    }
}
