package cc.sofast.infrastructure.extension;


import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * BizScenario（业务场景）= bizId + tenant + scenario, which can uniquely identify a user scenario.
 *
 * @author Frank Zhang
 * @date 2019-08-20 12:07
 */
public class BizScenario {
    public final static String DEFAULT_BIZ_ID = "#defaultBizId#";
    public final static String DEFAULT_TENANT = "#defaultTenant#";
    public final static String DEFAULT_SCENARIO = "#defaultScenario#";
    private final static String DOT_SEPARATOR = ".";

    /**
     * bizId is used to identify a business, such as "tmall", it's nullable if there is only one biz
     */
    private String bizId = DEFAULT_BIZ_ID;

    /**
     * tenant is used to identify a use case, such as "placeOrder", can not be null
     */
    private String tenant = DEFAULT_TENANT;

    /**
     * scenario is used to identify a use case, such as "88vip","normal", can not be null
     */
    private String scenario = DEFAULT_SCENARIO;


    private BizScenario() {
    }

    public static BizScenario valueOf(String bizId, String tenant, String scenario) {
        Assert.notNull(bizId, "bizId Null is not allowed.");
        BizScenario bizScenario = new BizScenario();
        bizScenario.bizId = bizId;
        bizScenario.tenant = tenant;
        bizScenario.scenario = scenario;
        return bizScenario;
    }

    public static BizScenario valueOf(String bizId, String tenant) {
        Assert.notNull(bizId, "bizId Null is not allowed.");
        if (StringUtils.hasText(tenant)) {
            return BizScenario.valueOf(bizId, tenant, DEFAULT_SCENARIO);
        }
        //返回默认租户
        return BizScenario.valueOf(bizId);
    }

    public static BizScenario valueOf(String bizId) {
        Assert.notNull(bizId, "bizId Null is not allowed.");
        return BizScenario.valueOf(bizId, DEFAULT_TENANT, DEFAULT_SCENARIO);
    }

    public static BizScenario newDefault() {
        return BizScenario.valueOf(DEFAULT_BIZ_ID, DEFAULT_TENANT, DEFAULT_SCENARIO);
    }

    public String getIdentityWithDefaultScenario() {
        return bizId + DOT_SEPARATOR + tenant + DOT_SEPARATOR + DEFAULT_SCENARIO;
    }

    public String getIdentityWithDefaultTenant() {
        return bizId + DOT_SEPARATOR + DEFAULT_TENANT + DOT_SEPARATOR + DEFAULT_SCENARIO;
    }

    /**
     * For above case, the BizScenario will be "tmall.placeOrder.88vip",
     * with this code, we can provide extension processing other than "tmall.placeOrder.normal" scenario.
     *
     * @return 识别
     */
    public String getUniqueIdentity() {
        return bizId + DOT_SEPARATOR + tenant + DOT_SEPARATOR + scenario;
    }
}

