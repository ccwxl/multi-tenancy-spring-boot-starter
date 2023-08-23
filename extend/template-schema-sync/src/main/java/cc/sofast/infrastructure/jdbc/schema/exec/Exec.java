package cc.sofast.infrastructure.jdbc.schema.exec;

import javax.sql.DataSource;

public interface Exec {

    boolean execSQL(DataSource dataSource, String peek, String sql);

}
