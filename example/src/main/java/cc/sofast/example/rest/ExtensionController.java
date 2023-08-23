package cc.sofast.example.rest;

import cc.sofast.example.bean.Account;
import cc.sofast.example.extension.AccountExtPt;
import cc.sofast.infrastructure.extension.BizScenario;
import cc.sofast.infrastructure.extension.TenantExtensionExecutor;
import cc.sofast.infrastructure.tenant.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author apple
 */
@RestController
@RequestMapping("/extension")
public class ExtensionController {

    @Autowired
    TenantExtensionExecutor tenantExtensionExecutor;

    @GetMapping
    public Account extension() {
        Account account = new Account();
        account.setAddress("济南");
        account.setAge(30L);
        account.setNickname("wxl");

        BizScenario scenario = BizScenario.valueOf(AccountExtPt.BIZ_ID, TenantContextHolder.peek(), AccountExtPt.SCENARIO);

        return tenantExtensionExecutor.execute(AccountExtPt.class, scenario,
                accountExtPt -> accountExtPt.accountCalculate(account));
    }
}
