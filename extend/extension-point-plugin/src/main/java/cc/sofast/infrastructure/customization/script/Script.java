package cc.sofast.infrastructure.customization.script;

import java.util.Map;

/**
 * @author wxl
 * 支持 spi 扩展
 */
public interface Script {

    String type();

    default void init() {
    }

    EngineExecutorResult eval(String script, Map<String, Object> param);

    default void destroy() {
    }

}
