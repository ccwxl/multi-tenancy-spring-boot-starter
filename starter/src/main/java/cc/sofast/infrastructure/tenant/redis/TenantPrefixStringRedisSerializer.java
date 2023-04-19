package cc.sofast.infrastructure.tenant.redis;

import cc.sofast.infrastructure.tenant.TenantContextHolder;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

/**
 * @author apple
 */
public class TenantPrefixStringRedisSerializer extends StringRedisSerializer {
    @Override
    public byte[] serialize(String key) {
        String tenant = TenantContextHolder.peek();
        if (StringUtils.hasText(tenant)) {
            key = tenant + ":" + key;
        }
        return super.serialize(key);
    }
}
