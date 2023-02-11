package cc.sofast.infrastructure.tenant.redis;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.RedisElementReader;
import org.springframework.data.redis.serializer.RedisElementWriter;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.Map;

/**
 * @author apple
 */
public class TenantPrefixRedisCacheManagerBuilderCustomizer implements RedisCacheManagerBuilderCustomizer {

    //org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration.createConfiguration
    @Override
    public void customize(RedisCacheManager.RedisCacheManagerBuilder builder) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new TenantPrefixStringRedisSerializer()));
        builder.cacheDefaults(redisCacheConfiguration);
    }
}
