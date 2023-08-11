package cc.sofast.infrastructure.customization.script;

import java.util.Objects;

/**
 * @author xielong.wang
 */
public class GroovyKey {

    private String tenant;

    private String key;

    private String script;

    public GroovyKey(String tenant, String key, String script) {
        this.tenant = tenant;
        this.key = key;
        this.script = script;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroovyKey groovyKey = (GroovyKey) o;
        return Objects.equals(tenant, groovyKey.tenant)
                && Objects.equals(key, groovyKey.key)
                && Objects.equals(script, groovyKey.script);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenant, key, script);
    }

    public String getUniqueKey() {

        return tenant + "_" + key;
    }
}
