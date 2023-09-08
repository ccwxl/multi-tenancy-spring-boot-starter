package cc.sofast.infrastructure.func;


import java.util.Objects;

/**
 * @author wxl
 */
public class Fun {

    private String tenant;

    private String biz;

    public Fun() {
    }

    public Fun(String tenant, String biz) {
        this.tenant = tenant;
        this.biz = biz;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public static Fun of(String tenant, String biz) {

        return new Fun(tenant, biz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fun tenantBizFun = (Fun) o;
        return Objects.equals(tenant, tenantBizFun.tenant) && Objects.equals(biz, tenantBizFun.biz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenant, biz);
    }
}
