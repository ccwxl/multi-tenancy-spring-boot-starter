package cc.sofast.example.rest;

import cc.sofast.example.service.SqlSync;
import cc.sofast.infrastructure.tenant.TenantBizExecutor;
import cc.sofast.infrastructure.tenant.TenantBizService;
import cc.sofast.infrastructure.tenant.TenantHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Predicate;

@RestController
@RequestMapping("/schema")
public class SchemaSyncController {

    @Autowired
    private TenantHelper tenantHelper;

    @Autowired
    private TenantBizExecutor tenantBizExecutor;

    @GetMapping
    public String schema(String sql) {
        List<String> tenantList = tenantHelper.getTenantList(Predicate.not(String::isEmpty));
        for (String tenant : tenantList) {
            tenantBizExecutor.execute(tenant, SqlSync.class, s -> { s.exec(sql); });
        }
        return "ok";
    }
}