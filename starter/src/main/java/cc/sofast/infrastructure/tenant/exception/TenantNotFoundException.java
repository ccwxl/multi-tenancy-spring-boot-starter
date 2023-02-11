package cc.sofast.infrastructure.tenant.exception;

/**
 * @author apple
 */
public class TenantNotFoundException extends TenantException {
    public TenantNotFoundException() {
        super("No TenantId found");
    }

    public TenantNotFoundException(String message) {
        super(message);
    }

    public TenantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
