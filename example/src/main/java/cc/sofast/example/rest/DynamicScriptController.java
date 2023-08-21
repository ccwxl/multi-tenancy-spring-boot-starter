package cc.sofast.example.rest;

import cc.sofast.infrastructure.customization.TenantDynamicScriptExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/script")
public class DynamicScriptController {

    @Autowired
    private TenantDynamicScriptExecutor tenantDynamicScriptExecutor;

    @GetMapping
    public String script() {

        return "";
    }
}
