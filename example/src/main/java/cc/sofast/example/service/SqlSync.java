package cc.sofast.example.service;

import cc.sofast.infrastructure.jdbc.schema.Schema;
import cc.sofast.infrastructure.jdbc.schema.exec.Exec;
import cc.sofast.infrastructure.tenant.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class SqlSync {

    @Autowired
    private Schema schema;

    @Autowired
    private DataSource dataSource;

    public void exec(String sql) {
        Exec exec = schema.getExec();
        exec.execSQL(dataSource, TenantContextHolder.peek(), sql);
    }
}
