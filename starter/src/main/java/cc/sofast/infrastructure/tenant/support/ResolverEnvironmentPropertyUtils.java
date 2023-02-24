package cc.sofast.infrastructure.tenant.support;

import cc.sofast.infrastructure.tenant.resolver.TenantResolverProperties;
import org.springframework.core.env.Environment;

/**
 * @author apple
 */
public class ResolverEnvironmentPropertyUtils {

    private ResolverEnvironmentPropertyUtils() {
        throw new IllegalStateException("Should not instantiate a utility class");
    }

    public static boolean tenantHttpWebResolverType(Environment environment, String propertySuffix,
                                                    String expectedPropertyValue) {
        String defaultValue = getDefaultPropertyValue(environment, ".web.", propertySuffix);
        if (defaultValue == null) {
            return false;
        }
        return defaultValue.equalsIgnoreCase(expectedPropertyValue);
    }

    private static String getDefaultPropertyValue(Environment environment, String prefix, String propertySuffix) {
        return environment.getProperty(TenantResolverProperties.PREFIX + prefix + propertySuffix);
    }

    public static boolean tenantResolverType(Environment environment, String propertySuffix, String expectedPropertyValue) {
        String defaultValue = getDefaultPropertyValue(environment, ".", propertySuffix);
        if (defaultValue == null) {
            return false;
        }
        return defaultValue.equalsIgnoreCase(expectedPropertyValue);
    }
}
