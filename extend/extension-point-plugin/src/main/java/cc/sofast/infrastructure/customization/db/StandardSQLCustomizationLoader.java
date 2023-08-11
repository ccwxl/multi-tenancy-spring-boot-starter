package cc.sofast.infrastructure.customization.db;


import cc.sofast.infrastructure.customization.TKey;

/**
 * @author wxl
 * 从数据库中加载租户的定制化配置项的值
 */
public class StandardSQLCustomizationLoader extends JdbcCustomizationLoader {

    @Override
    protected String getSaveOrUpdateSQL(TKey tKey, String valJson) {

        return null;
    }

    @Override
    protected String getDeleteSQL(TKey tKey) {

        return null;
    }

    @Override
    protected String getValSQL(TKey tKey) {

        return null;
    }
}
