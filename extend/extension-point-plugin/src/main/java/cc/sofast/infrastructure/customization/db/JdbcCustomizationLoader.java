package cc.sofast.infrastructure.customization.db;

import cc.sofast.infrastructure.customization.PersistentCustomizationLoader;
import cc.sofast.infrastructure.customization.TKey;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wxl
 * jdbc 持久化方式
 */
public abstract class JdbcCustomizationLoader extends PersistentCustomizationLoader {

    private JdbcTemplate jdbcTemplate;

    @Override
    public String val(TKey key) {
        String getSQL = getValSQL(key);
        return jdbcTemplate.queryForObject(getSQL, ResultSet::getString);
    }

    public boolean remove(TKey key) {
        int update = jdbcTemplate.update(getDeleteSQL(key));
        return update > 0;
    }

    public boolean saveOrUpdate(TKey key, String valJson) {
        int update = jdbcTemplate.update(getSaveOrUpdateSQL(key, valJson));
        return update>0;
    }

    protected abstract String getSaveOrUpdateSQL(TKey tKey, String valJson);

    protected abstract String getDeleteSQL(TKey tKey);

    protected abstract String getValSQL(TKey tKey);

}
