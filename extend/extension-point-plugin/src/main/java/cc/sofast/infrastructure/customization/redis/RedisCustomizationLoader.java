package cc.sofast.infrastructure.customization.redis;


import cc.sofast.infrastructure.customization.CustomizationLoader;import cc.sofast.infrastructure.customization.TKey;

/**
 * @author wxl
 */
public class RedisCustomizationLoader implements CustomizationLoader {

    @Override
    public String val(TKey key) {

        return null;
    }

    @Override
    public boolean remove(TKey key) {

        return true;
    }

    @Override
    public boolean saveOrUpdate(TKey key, String valJson) {

        return true;
    }
}
