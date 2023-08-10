package cc.sofast.infrastructure.customization;


import cc.sofast.infrastructure.customization.db.JdbcCustomizationLoader;
import cc.sofast.infrastructure.customization.mem.MemCustomizationLoader;
import org.springframework.util.StringUtils;

/**
 * @author wxl
 * 组合缓存和持久化存储加载器。实现多级加载
 */
public class ComposeCustomizationLoader implements CustomizationLoader {

    private final MemCustomizationLoader memCustomizationLoader;

    private final PersistentCustomizationLoader persistentCustomizationLoader;


    public ComposeCustomizationLoader(MemCustomizationLoader memCustomizationLoader,
                                      PersistentCustomizationLoader persistentCustomizationLoader) {
        this.memCustomizationLoader = memCustomizationLoader;
        this.persistentCustomizationLoader = persistentCustomizationLoader;
    }


    @Override
    public String val(TKey key) {
        String val = memCustomizationLoader.val(key);
        if (StringUtils.hasLength(val)) {
            return val;
        }
        String dbVal = persistentCustomizationLoader.val(key);
        memCustomizationLoader.saveOrUpdate(key, dbVal);
        return dbVal;
    }

    @Override
    public boolean remove(TKey key) {
        memCustomizationLoader.remove(key);
        persistentCustomizationLoader.remove(key);
        return false;
    }

    @Override
    public boolean saveOrUpdate(TKey key, String valJson) {
        memCustomizationLoader.saveOrUpdate(key, valJson);
        persistentCustomizationLoader.saveOrUpdate(key, valJson);
        return false;
    }
}