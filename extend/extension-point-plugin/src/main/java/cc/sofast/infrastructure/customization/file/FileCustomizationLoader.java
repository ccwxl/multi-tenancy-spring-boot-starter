package cc.sofast.infrastructure.customization.file;


import cc.sofast.infrastructure.customization.PersistentCustomizationLoader;
import cc.sofast.infrastructure.customization.TKey;

/**
 * @author wxl
 * 命名规则为 tenant.json
 * {
 *  "key":value
 * }
 */
public class FileCustomizationLoader extends PersistentCustomizationLoader {

    @Override
    public String val(TKey key) {

        return null;
    }

    @Override
    public boolean remove(TKey key) {
        //什么也不做
        return false;
    }

    @Override
    public boolean saveOrUpdate(TKey key, String valJson) {
        //什么也不做
        return false;
    }
}
