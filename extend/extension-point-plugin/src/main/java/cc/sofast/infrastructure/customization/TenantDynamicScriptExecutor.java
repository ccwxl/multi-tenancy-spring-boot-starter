package cc.sofast.infrastructure.customization;


import java.util.Map;

/**
 * @author wxl
 * 脚本执行器。支持  AviatorExecutor, GroovyExecutor, DroolsExecutor
 * 支持某个业务每个租户业务（金额，优惠。）定制化计算规则，转换规则，过滤规则等
 */
public class TenantDynamicScriptExecutor {

    private TenantCustomization tenantCustomization;

    private ScriptExecutor scriptExecutor;

    public Object eval(TKey key, Map<String, Object> param) {
        //将公共字段放入到参数上去
        param.put("tenant", key.getTenant());
        param.put("key", key.getKey());
        DynamicScriptModel dsm = tenantCustomization.getVal(key, DynamicScriptModel.class);
        return scriptExecutor.eval(dsm, param);
    }
}
