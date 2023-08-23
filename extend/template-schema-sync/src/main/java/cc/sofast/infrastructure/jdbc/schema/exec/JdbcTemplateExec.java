package cc.sofast.infrastructure.jdbc.schema.exec;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author apple
 */
public class JdbcTemplateExec implements Exec {

    @Override
    public boolean execSQL(DataSource dataSource, String peek, String sql) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(sql);
        return false;
    }
}
