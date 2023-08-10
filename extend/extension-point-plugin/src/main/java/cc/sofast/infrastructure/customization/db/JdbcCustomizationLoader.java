package cc.sofast.infrastructure.customization.db;

import cc.sofast.infrastructure.customization.PersistentCustomizationLoader;
import cc.sofast.infrastructure.customization.TKey;

/**
 * @author wxl
 */
public abstract class JdbcCustomizationLoader extends PersistentCustomizationLoader {

    @Override
    public String val(TKey key) {
        String querySQL = getSQL();

        return null;
    }

    protected abstract String getSQL();

}
