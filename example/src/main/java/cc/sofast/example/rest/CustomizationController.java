package cc.sofast.example.rest;

import cc.sofast.example.bean.Account;
import cc.sofast.infrastructure.customization.TKey;
import cc.sofast.infrastructure.customization.TenantCustomization;
import cc.sofast.infrastructure.tenant.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customization")
public class CustomizationController {

    @Autowired
    private TenantCustomization tenantCustomization;

    private static final String MACHINE_PROFILE = "MACHINE_PROFILE";

    @GetMapping()
    public Account getConfigItem() {
        return tenantCustomization.getVal(TKey.of(TenantContextHolder.peek(), MACHINE_PROFILE), Account.class);
    }

    @PutMapping
    public String updateConfigItem(@RequestBody Account account) {
        boolean b = tenantCustomization.saveOrUpdate(TKey.of(TenantContextHolder.peek(), MACHINE_PROFILE), account);
        return b ? "ok" : "failed";
    }

    @DeleteMapping
    public String deleteConfigItem() {
        boolean remove = tenantCustomization.remove(TKey.of(TenantContextHolder.peek(), MACHINE_PROFILE));
        return remove ? "ok" : "failed";
    }
}
