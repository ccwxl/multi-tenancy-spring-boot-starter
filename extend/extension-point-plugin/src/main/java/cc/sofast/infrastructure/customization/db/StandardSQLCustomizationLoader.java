package cc.sofast.infrastructure.customization.db;


import cc.sofast.infrastructure.customization.TCustomizationModel;
import cc.sofast.infrastructure.customization.TKey;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

import java.sql.Types;

/**
 * @author wxl
 * 从数据库中加载租户的定制化配置项的值
 * Pair<sql,BeanPropertySqlParameterSource>
 */
public class StandardSQLCustomizationLoader extends JdbcCustomizationLoader {

    public StandardSQLCustomizationLoader(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    protected Pair<String, BeanPropertySqlParameterSource> getSaveOrUpdateSQL(TKey tKey, String valJson) {
        String val = val(tKey);
        String execSQL;
        if (StringUtils.hasText(val)) {
            execSQL = "UPDATE customization set val=:value where tenant=:tenant and key=:key";
        } else {
            execSQL = "insert into customization(tenant,key,val) VALUES(:tenant,:key,:value)";
        }
        TCustomizationModel tcm = new TCustomizationModel();
        tcm.setTenant(tKey.getTenant());
        tcm.setKey(tKey.getKey());
        tcm.setValue(valJson);
        BeanPropertySqlParameterSource mapParam = new BeanPropertySqlParameterSource(tcm);
        mapParam.registerSqlType("tenant", Types.VARCHAR);
        mapParam.registerSqlType("key", Types.VARCHAR);
        mapParam.registerSqlType("value", Types.OTHER);
        return Pair.of(execSQL, mapParam);
    }

    @Override
    protected Pair<String, BeanPropertySqlParameterSource> getDeleteSQL(TKey tKey) {
        String deleteSQL = "delete from customization where key=:key and tenant=:tenant";
        TCustomizationModel tcm = new TCustomizationModel();
        tcm.setTenant(tKey.getTenant());
        tcm.setKey(tKey.getKey());
        BeanPropertySqlParameterSource mapParam = new BeanPropertySqlParameterSource(tcm);
        mapParam.registerSqlType("tenant", Types.VARCHAR);
        mapParam.registerSqlType("key", Types.VARCHAR);
        return Pair.of(deleteSQL, mapParam);
    }

    @Override
    protected Pair<String, BeanPropertySqlParameterSource> getValSQL(TKey tKey) {
        String selectSQL = "select val from customization where key=:key and tenant=:tenant";
        TCustomizationModel tcm = new TCustomizationModel();
        tcm.setTenant(tKey.getTenant());
        tcm.setKey(tKey.getKey());
        BeanPropertySqlParameterSource mapParam = new BeanPropertySqlParameterSource(tcm);
        mapParam.registerSqlType("tenant", Types.VARCHAR);
        mapParam.registerSqlType("key", Types.VARCHAR);
        return Pair.of(selectSQL, mapParam);
    }
}
