package cc.sofast.infrastructure.customization.redis;


import cc.sofast.infrastructure.customization.CustomizationLoader;import cc.sofast.infrastructure.customization.TKey;

/**
 * @author wxl
 */
public class RedisCustomizationLoader implements CustomizationLoader {

    @Override
    public String val(TKey key) {

        return "[1,2]";
    }

    @Override
    public boolean remove(TKey key) {

        return false;
    }

    @Override
    public boolean saveOrUpdate(TKey key, String valJson) {


        return false;
    }
}
