package cc.sofast.infrastructure.tenant.notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ErrorHandler;

/**
 * @author apple
 */
@Slf4j
public class TenantEventListenerErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        log.error("tenant event process failed. [{}] ", t.getMessage());
    }
}
