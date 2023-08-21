package cc.sofast.infrastructure.customization.db;

import cc.sofast.infrastructure.customization.PersistentCustomizationLoader;
import cc.sofast.infrastructure.customization.TKey;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author wxl
 * jdbc 持久化方式
 */
public abstract class JdbcCustomizationLoader extends PersistentCustomizationLoader {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcCustomizationLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public String val(TKey key) {
        Pair<String, BeanPropertySqlParameterSource> valSQL = getValSQL(key);
        List<String> results = namedParameterJdbcTemplate.queryForList(valSQL.getFirst(), valSQL.getSecond(), String.class);
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }
        if (results.size() > 1) {
            return results.get(0);
        }
        return results.iterator().next();
    }

    public boolean remove(TKey key) {
        Pair<String, BeanPropertySqlParameterSource> deleteSQL = getDeleteSQL(key);
        int update = namedParameterJdbcTemplate.update(deleteSQL.getFirst(), deleteSQL.getSecond());
        return update > 0;
    }

    public boolean saveOrUpdate(TKey key, String valJson) {
        Pair<String, BeanPropertySqlParameterSource> saveSQL = getSaveOrUpdateSQL(key, valJson);
        int update = namedParameterJdbcTemplate.update(saveSQL.getFirst(), saveSQL.getSecond());
        return update > 0;
    }

    protected abstract Pair<String, BeanPropertySqlParameterSource> getSaveOrUpdateSQL(TKey tKey, String valJson);

    protected abstract Pair<String, BeanPropertySqlParameterSource> getDeleteSQL(TKey tKey);

    protected abstract Pair<String, BeanPropertySqlParameterSource> getValSQL(TKey tKey);

}
