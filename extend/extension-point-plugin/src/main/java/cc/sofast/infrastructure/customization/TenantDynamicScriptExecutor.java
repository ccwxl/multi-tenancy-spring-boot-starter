package cc.sofast.infrastructure.customization;


import cc.sofast.infrastructure.customization.script.EngineExecutorResult;

import java.util.Map;

/**
 * @author wxl
 * 脚本执行器。支持  AviatorExecutor, GroovyExecutor, DroolsExecutor
 * 支持某个业务每个租户业务（金额，优惠。）定制化计算规则，转换规则，过滤规则等
 */
public class TenantDynamicScriptExecutor {

    private TenantCustomization tenantCustomization;

    private ScriptExecutor scriptExecutor;

    public EngineExecutorResult eval(TKey key, Map<String, Object> param) {
        //将公共字段放入到参数上去
        param.put("tenant", key.getTenant());
        param.put("key", key.getKey());
        DynamicScriptModel dsm = tenantCustomization.getVal(key, DynamicScriptModel.class);
        if (dsm == null) {
            //说明当前的租户没有定制化脚本要执行，直接返回
            return EngineExecutorResult.success(Cons.SUCCESS);
        }
        return scriptExecutor.eval(dsm, param);
    }

    public void setTenantCustomization(TenantCustomization tenantCustomization) {
        this.tenantCustomization = tenantCustomization;
    }

    public void setScriptExecutor(ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
    }
}
