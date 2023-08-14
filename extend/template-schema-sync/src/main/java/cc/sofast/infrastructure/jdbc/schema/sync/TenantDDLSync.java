package cc.sofast.infrastructure.jdbc.schema.sync;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import cc.sofast.infrastructure.jdbc.schema.exec.Exec;

/**
 * @author apple
 */
public class TenantDDLSync {

    private Exec exec;

    public TenantDDLSync(Exec exec) {
        this.exec = exec;
    }

    public boolean createSchemaForm(SchemaInfo source, SchemaInfo target) {

        return false;
    }

}
