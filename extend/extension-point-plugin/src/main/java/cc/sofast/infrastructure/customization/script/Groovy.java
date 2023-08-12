package cc.sofast.infrastructure.customization.script;

import cc.sofast.infrastructure.customization.ApplicationContextHelper;
import cc.sofast.infrastructure.customization.Cons;
import com.google.auto.service.AutoService;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wxl
 * groovy 脚本动态执行
 */
@AutoService(Script.class)
public class Groovy implements Script {

    private final Map<GroovyKey, GroovyScriptEntry> SCRIPT_CACHE = new ConcurrentHashMap<>();

    private static final Logger log = LoggerFactory.getLogger(Groovy.class);

    @Override
    public String type() {

        return "groovy";
    }

    @Override
    public EngineExecutorResult eval(String script, Map<String, Object> param) {
        //计算下脚本的md5，从缓存执行 script 当做key去加载对应的Class.
        String tenant = String.valueOf(param.get(Cons.TENANT));
        String key = String.valueOf(param.get(Cons.KEY));
        GroovyScriptEntry scriptEntry = SCRIPT_CACHE.computeIfAbsent(new GroovyKey(tenant, key, script), key1 -> {
            //脚本编译
            try (GroovyClassLoader gcl = new GroovyClassLoader()) {
                // 以 GroovyCompiler + 脚本的名称作为类名称
                String fingerprint = DigestUtils.md5DigestAsHex(key1.getScript().getBytes());
                // 创建脚本对象
                GroovyScriptEntry scriptEntry1 = new GroovyScriptEntry(key1.getUniqueKey(), script, fingerprint, System.currentTimeMillis());
                // 动态加载脚本为Class
                Class<?> clazz = gcl.parseClass(key1.getScript(), Groovy.class.getSimpleName() + "_" + key1.getTenant() + "_" + key1.getKey());
                scriptEntry1.setClazz(clazz);

                return scriptEntry1;
            } catch (IOException e) {
                log.error("compile groovy script error", e);
            }
            return null;
        });
        if (scriptEntry == null) {
            throw new RuntimeException("script is null");
        }

        return execute(scriptEntry, param);
    }

    private EngineExecutorResult execute(GroovyScriptEntry scriptEntry, Map<String, Object> param) {
        Object result;
        try {
            // 构建binding入参
            Binding binding = buildBinding(param);
            Assert.notNull(scriptEntry.getClazz(), "execute script failed, clazz can not be null.");
            // 创建脚本（可以看到这里就是基于Class去new一个script对象）,TODO 这个对象是否可以缓存
            // see https://blog.csdn.net/feiqinbushizheng/article/details/108582634
            //https://mp.weixin.qq.com/s?__biz=MzU0OTE4MzYzMw==&mid=2247517100&idx=5&sn=bbb34817a6fbf16cc6e9897da335033d&chksm=fbb10a52ccc6834483ef06c38c5bce3e902082bbb8f82b5c8906d51b91a3f48dc4edecff10b5&scene=27
            groovy.lang.Script script = InvokerHelper.createScript(scriptEntry.getClazz(), binding);
            script.setBinding(binding);
            // 执行脚本
            result = script.run();
        } catch (Exception ex) {
            log.error("execute groovy script error, scriptEntry is : {}," +
                    " executeParams is : {}", scriptEntry, param, ex);
            return EngineExecutorResult.failed(ex);
        }
        return EngineExecutorResult.success(result);
    }

    /**
     * 构建binding信息
     */
    private Binding buildBinding(Map<String, Object> params) {
        Binding binding = new Binding();
        // 将spring容器上下文放入脚本
        binding.setProperty(Cons.APPLICATION_CONTEXT, ApplicationContextHelper.getContext());
        // 没有需要传递的参数
        if (Objects.isNull(params)) {
            return binding;
        }
        params.forEach(binding::setProperty);
        return binding;
    }
}
