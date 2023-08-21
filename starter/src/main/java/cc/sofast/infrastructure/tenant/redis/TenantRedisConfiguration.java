package cc.sofast.infrastructure.tenant.redis;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author apple
 * redis 层多租户配置
 */
@Configuration
public class TenantRedisConfiguration {

    /**
     * 不带解析租户标识的redisTemplate
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @param customizers            customizers
     * @return RedisTemplate<String, Object>
     */
    @Bean("noneTenantRedisTemplate")
    public RedisTemplate<String, Object> noneTenantRedisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                                 ObjectProvider<RedisTemplateCustomizer> customizers) {
        RedisTemplate<String, Object> redisTemplate = new TenantRedisConfiguration().redisTemplate(redisConnectionFactory, customizers);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

    /**
     * 解析租户标识的 RedisTemplate
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @param customizers            ObjectProvider<RedisTemplateCustomizer>
     * @return RedisTemplate<String, Object>
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       ObjectProvider<RedisTemplateCustomizer> customizers) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new TenantPrefixStringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        customizers.orderedStream().forEach((customizer) -> customizer.customize(redisTemplate));
        return redisTemplate;
    }

    /**
     * 不解析租户标识的stringRedisTemplate
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @return StringRedisTemplate
     */
    @Bean("noneTenantStringRedisTemplate")
    public StringRedisTemplate noneTenantStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new TenantRedisConfiguration().stringRedisTemplate(redisConnectionFactory);
        stringRedisTemplate.setKeySerializer(RedisSerializer.string());
        return stringRedisTemplate;
    }

    /**
     * 解析租户标识的 StringRedisTemplate
     *
     * @param redisConnectionFactory RedisConnectionFactory
     * @return StringRedisTemplate
     */
    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(redisConnectionFactory);
        stringRedisTemplate.setKeySerializer(new TenantPrefixStringRedisSerializer());
        stringRedisTemplate.setValueSerializer(RedisSerializer.string());
        stringRedisTemplate.setHashKeySerializer(RedisSerializer.string());
        stringRedisTemplate.setHashValueSerializer(RedisSerializer.string());
        return stringRedisTemplate;
    }

    /**
     * spring redis cache 适配
     *
     * @param cacheProperties {@link CacheProperties}
     * @return {@link RedisCacheConfiguration}
     */
    @Bean
    public RedisCacheConfiguration createConfiguration(CacheProperties cacheProperties) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        //key 序列化
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new TenantPrefixStringRedisSerializer()));
        //value 序列化
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

}
