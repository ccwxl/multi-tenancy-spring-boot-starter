package cc.sofast.infrastructure.customization;

/**
 * @author wxl
 * 租户自定义配置加载器
 */
public interface CustomizationLoader {

    String val(TKey key);

    boolean remove(TKey key);

    boolean saveOrUpdate(TKey key, String valJson);
}
