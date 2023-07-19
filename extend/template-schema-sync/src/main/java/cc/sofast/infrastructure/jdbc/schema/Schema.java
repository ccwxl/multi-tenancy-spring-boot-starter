package cc.sofast.infrastructure.jdbc.schema;

import cc.sofast.infrastructure.jdbc.schema.diff.Diff;
import cc.sofast.infrastructure.jdbc.schema.diff.SchemaDiff;
import cc.sofast.infrastructure.jdbc.schema.jdbc.Exec;
import cc.sofast.infrastructure.jdbc.schema.jdbc.JdbcExec;
import cc.sofast.infrastructure.jdbc.schema.sync.DdlSync;

/**
 * @author apple
 */
public class Schema {

    private Diff diff;

    private Exec exec;

    private DdlSync ddlSync;

    private String workDir;

    public Diff getDiff() {
        return diff;
    }

    public Exec getExec() {
        return exec;
    }

    public DdlSync getDdlSync() {
        return ddlSync;
    }

    public String getWorkDir() {
        return workDir;
    }

    public Schema() {
        this(new Builder());
    }

    public Schema(Builder builder) {
        this.diff = builder.diff;
        this.exec = builder.exec;
        this.ddlSync = builder.ddlSync;
        this.workDir = builder.workDir;
    }

    public void init() {
        //将txz文件解压到工作目录

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Diff diff;

        private Exec exec;

        private DdlSync ddlSync;
        private String workDir;

        public Builder(Schema schema) {
            this.diff = schema.diff;
            this.exec = schema.exec;
            this.ddlSync = schema.ddlSync;
            this.workDir = schema.workDir;
        }

        public Builder() {
            diff = new SchemaDiff();
            exec = new JdbcExec();
            ddlSync = new DdlSync(exec);
            workDir = System.getProperty("java.io.tmpdir");
        }

        public Builder diff(Diff diff) {
            this.diff = diff;
            return this;
        }

        public Builder workDir(String workDir) {
            this.workDir = workDir;
            return this;
        }

        public Builder exec(Exec exec) {
            this.exec = exec;
            return this;
        }

        public Builder ddlSync(DdlSync ddlSync) {
            this.ddlSync = ddlSync;
            return this;
        }

        public Schema build() {

            return new Schema(this);
        }
    }
}
