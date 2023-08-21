package cc.sofast.infrastructure.customization.redis;


import cc.sofast.infrastructure.customization.CustomizationLoader;
import cc.sofast.infrastructure.customization.TKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author wxl
 */
public class RedisCustomizationLoader implements CustomizationLoader {

    private final StringRedisTemplate stringRedisTemplate;

    private static final String KEY_FORMAT = "script_%s_%s";

    public RedisCustomizationLoader(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String val(TKey key) {
        String redisKey = getRedisKey(key);
        return stringRedisTemplate.opsForValue().get(redisKey);
    }

    private static String getRedisKey(TKey key) {

        return String.format(KEY_FORMAT, key.getTenant(), key.getKey());
    }

    @Override
    public boolean remove(TKey key) {

        return Boolean.TRUE.equals(stringRedisTemplate.delete(getRedisKey(key)));
    }

    @Override
    public boolean saveOrUpdate(TKey key, String valJson) {
        stringRedisTemplate.opsForValue().set(getRedisKey(key), valJson);
        return true;
    }
}
