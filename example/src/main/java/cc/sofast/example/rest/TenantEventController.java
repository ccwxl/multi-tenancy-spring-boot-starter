package cc.sofast.example.rest;

import cc.sofast.infrastructure.tenant.datasource.DataSourceProperty;
import cc.sofast.infrastructure.tenant.notify.TenantEvent;
import cc.sofast.infrastructure.tenant.notify.TenantEventType;
import cc.sofast.infrastructure.tenant.notify.TenantNotify;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * @author apple
 */
@RestController
@RequestMapping("tenant")
public class TenantEventController {

    private final TenantNotify tenantNotify;

    public TenantEventController(TenantNotify tenantNotify) {
        this.tenantNotify = tenantNotify;
    }

    @PostMapping("/event")
    public String addTenant() {
        TenantEvent tenantEvent = new TenantEvent();
        tenantEvent.setType(TenantEventType.CREATE);
        tenantEvent.setTenants(Collections.singletonList("t1"));
        tenantEvent.setDsType("hikacp");
        tenantNotify.pubTenantEvent(tenantEvent);
        return "ok";
    }
}
