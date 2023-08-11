package cc.sofast.infrastructure.customization.db;

import cc.sofast.infrastructure.customization.PersistentCustomizationLoader;
import cc.sofast.infrastructure.customization.TKey;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.util.Assert;

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
        Pair<String, BeanPropertySqlParameterSource> valSQL = getValSQL(key);
        return jdbcTemplate.queryForObject(valSQL.getFirst(), ResultSet::getString, valSQL.getSecond());
    }

    public boolean remove(TKey key) {
        Pair<String, BeanPropertySqlParameterSource> deleteSQL = getDeleteSQL(key);
        int update = jdbcTemplate.update(deleteSQL.getFirst(), deleteSQL.getSecond());
        return update > 0;
    }

    public boolean saveOrUpdate(TKey key, String valJson) {
        Pair<String, BeanPropertySqlParameterSource> saveSQL = getSaveOrUpdateSQL(key, valJson);
        int update = jdbcTemplate.update(saveSQL.getFirst(), saveSQL.getSecond());
        return update > 0;
    }

    protected abstract Pair<String, BeanPropertySqlParameterSource> getSaveOrUpdateSQL(TKey tKey, String valJson);

    protected abstract Pair<String, BeanPropertySqlParameterSource> getDeleteSQL(TKey tKey);

    protected abstract Pair<String, BeanPropertySqlParameterSource> getValSQL(TKey tKey);

}
