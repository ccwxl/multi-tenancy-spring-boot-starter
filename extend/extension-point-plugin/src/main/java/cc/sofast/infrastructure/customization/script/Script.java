package cc.sofast.infrastructure.customization.script;

import java.util.Map;

/**
 * @author wxl
 * 支持 spi 扩展
 */
public interface Script {

    void init();

    Object eval(String script, Map<String, Object> param);

    void destroy();
}
