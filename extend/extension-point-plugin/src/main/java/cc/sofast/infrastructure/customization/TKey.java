package cc.sofast.infrastructure.customization;


import java.util.Objects;

/**
 * @author wxl
 * key 根据key加载租户配置。
 */
public class TKey {

    private String tenant;

    private String key;

    public TKey() {
    }

    public TKey(String tenant, String key) {
        this.tenant = tenant;
        this.key = key;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TKey tKey = (TKey) o;
        return Objects.equals(tenant, tKey.tenant) && Objects.equals(key, tKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenant, key);
    }

    public static TKey of(String tenant, String key) {

        return new TKey(tenant, key);
    }
}
