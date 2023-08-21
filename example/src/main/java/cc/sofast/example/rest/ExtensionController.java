package cc.sofast.example.rest;

import cc.sofast.infrastructure.extension.TenantExtensionExecutor;
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
    public String extension() {

        return "";
    }
}
