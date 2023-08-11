package cc.sofast.infrastructure.customization;

import cc.sofast.infrastructure.customization.file.FileCustomizationLoader;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class TenantDynamicScriptExecutorTest {

    @Test
    void eval() {
        TenantDynamicScriptExecutor tds = new TenantDynamicScriptExecutor();
        tds.setTenantCustomization(new TenantCustomization(new FileCustomizationLoader()));
        tds.setScriptExecutor(new ScriptExecutor());

        TKey tKey = new TKey();
        tKey.setTenant("t");
        tKey.setKey("script");

        Map<String,Object> param=new HashMap<>();
        Object eval = tds.eval(tKey, param);
        System.out.println(eval);
    }
}