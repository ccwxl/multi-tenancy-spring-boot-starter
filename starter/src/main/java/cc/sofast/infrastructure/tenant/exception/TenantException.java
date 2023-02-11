package cc.sofast.infrastructure.tenant.exception;

/**
 * @author wxl
 */
public class TenantException extends RuntimeException {

    public TenantException(String message) {
        super(message);
    }

    public TenantException(String message, Throwable cause) {
        super(message, cause);
    }
}
