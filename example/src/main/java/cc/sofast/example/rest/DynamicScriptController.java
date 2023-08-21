package cc.sofast.example.rest;

import cc.sofast.example.bean.Account;
import cc.sofast.infrastructure.customization.DynamicScriptModel;
import cc.sofast.infrastructure.customization.TKey;
import cc.sofast.infrastructure.customization.TenantCustomization;
import cc.sofast.infrastructure.customization.TenantDynamicScriptExecutor;
import cc.sofast.infrastructure.customization.script.EngineExecutorResult;
import cc.sofast.infrastructure.tenant.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.tools.JavaCompiler;
import java.util.HashMap;
import java.util.Map;

/**
 * @author apple
 */
@RestController
@RequestMapping("/script")
public class DynamicScriptController {

    @Autowired
    private TenantDynamicScriptExecutor tenantDynamicScriptExecutor;

    @Autowired
    private TenantCustomization tenantCustomization;

    private static final String ACCOUNT_CONVERTER = "ACCOUNT_CONVERTER";

    @GetMapping
    public EngineExecutorResult script() {
        Account account = new Account();
        account.setAddress("济南");
        account.setAge(30L);
        account.setNickname("wxl");
        TKey tKey = TKey.of(TenantContextHolder.peek(), ACCOUNT_CONVERTER);
        Map<String, Object> accountParam = new HashMap<>(1);
        accountParam.put("account", account);
        return tenantDynamicScriptExecutor.eval(tKey, accountParam);
    }

    @PostMapping
    public DynamicScriptModel register(DynamicScriptModel dynamicScriptModel) {
        TKey tKey = TKey.of(TenantContextHolder.peek(), ACCOUNT_CONVERTER);
        tenantCustomization.saveOrUpdate(tKey, dynamicScriptModel);
        return tenantCustomization.getVal(tKey, DynamicScriptModel.class);
    }
}
