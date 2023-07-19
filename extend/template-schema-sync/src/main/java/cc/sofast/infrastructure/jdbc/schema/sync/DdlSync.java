package cc.sofast.infrastructure.jdbc.schema.sync;

import cc.sofast.infrastructure.jdbc.schema.jdbc.Exec;

/**
 * @author apple
 */
public class DdlSync {

    private Exec exec;

    public DdlSync(Exec exec) {
        this.exec=exec;
    }
}
