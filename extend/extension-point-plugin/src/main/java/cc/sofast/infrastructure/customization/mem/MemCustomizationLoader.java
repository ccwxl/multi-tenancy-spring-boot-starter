package cc.sofast.infrastructure.customization.mem;


import cc.sofast.infrastructure.customization.TKey;
import cc.sofast.infrastructure.customization.redis.RedisCustomizationLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wxl
 */
public class MemCustomizationLoader extends RedisCustomizationLoader {

    private static final Map<TKey, String> KV = new ConcurrentHashMap<>();

    @Override
    public String val(TKey key) {
        return KV.computeIfAbsent(key, this::superVal);
    }

    private String superVal(TKey key) {

        return super.val(key);
    }

    @Override
    public boolean remove(TKey key) {
        KV.remove(key);
        return super.remove(key);
    }

    @Override
    public boolean saveOrUpdate(TKey key, String valJson) {
        KV.put(key, valJson);
        return super.saveOrUpdate(key, valJson);
    }
}