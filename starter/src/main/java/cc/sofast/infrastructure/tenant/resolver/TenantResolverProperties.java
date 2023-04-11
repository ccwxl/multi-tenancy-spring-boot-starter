package cc.sofast.infrastructure.tenant.resolver;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author apple
 */
@ConfigurationProperties(prefix = TenantResolverProperties.PREFIX)
public class TenantResolverProperties {
    public static final String PREFIX = "sofast.tenant.resolver";

    private Type type;
    private WebResolver web;
    private FixedResolver fixed;
    private SystemPropertiesResolver system;
    private List<String> ignorePath;

    public static class WebResolver {
        private WebType webType;

        /**
         * 识别码
         */
        private String id = "X-Tenant";

        public int stripPrefix = 1;

        public WebType getWebType() {
            return webType;
        }

        public void setWebType(WebType webType) {
            this.webType = webType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getStripPrefix() {
            return stripPrefix;
        }

        public void setStripPrefix(int stripPrefix) {
            this.stripPrefix = stripPrefix;
        }
    }

    public static class FixedResolver {

        public String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class SystemPropertiesResolver {
        /**
         * 识别码
         */
        private String id = "tenant";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public WebResolver getWeb() {
        return web;
    }

    public void setWeb(WebResolver web) {
        this.web = web;
    }

    public FixedResolver getFixed() {
        return fixed;
    }

    public void setFixed(FixedResolver fixed) {
        this.fixed = fixed;
    }

    public SystemPropertiesResolver getSystem() {
        return system;
    }

    public void setSystem(SystemPropertiesResolver system) {
        this.system = system;
    }

    public List<String> getIgnorePath() {
        return ignorePath;
    }

    public void setIgnorePath(List<String> ignorePath) {
        this.ignorePath = ignorePath;
    }
}