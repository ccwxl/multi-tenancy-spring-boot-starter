package cc.sofast.infrastructure.tenant.redis;

import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author apple
 */
public class TenantPrefixStringRedisSerializer extends StringRedisSerializer {

    @Override
    public byte[] serialize(String string) {
        //增加
        return super.serialize(string);
    }
}
