package cc.sofast.infrastructure.tenant.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author apple
 */
public interface RedisTemplateCustomizer {

    /**
     * redisTemplate 自定义
     *
     * @param redisTemplate RedisTemplate<String, Object>
     */
    void customize(RedisTemplate<String, Object> redisTemplate);
}
