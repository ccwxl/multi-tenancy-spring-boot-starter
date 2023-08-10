package cc.sofast.infrastructure.customization.db;


import cc.sofast.infrastructure.customization.TKey;

/**
 * @author wxl
 * 从数据库中加载租户的定制化配置项的值
 */
public class StandardSQLCustomizationLoader extends JdbcCustomizationLoader {

    @Override
    protected String getSQL() {

        return null;
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
