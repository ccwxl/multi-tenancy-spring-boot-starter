package cc.sofast.infrastructure.jdbc.schema;

import cc.sofast.infrastructure.jdbc.schema.diff.Diff;
import cc.sofast.infrastructure.jdbc.schema.diff.SchemaDiff;
import cc.sofast.infrastructure.jdbc.schema.jdbc.Exec;
import cc.sofast.infrastructure.jdbc.schema.jdbc.JdbcExec;
import cc.sofast.infrastructure.jdbc.schema.sync.DdlSync;
import cc.sofast.infrastructure.jdbc.schema.utils.OSUtils;
import cc.sofast.infrastructure.jdbc.schema.utils.PgDumpUtils;
import cc.sofast.infrastructure.jdbc.schema.utils.TzxUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author apple
 */
@Getter
@Slf4j
public class Schema {
    public static final String RWXR_XR_X = "rwxr-xr-x";

    private final Diff diff;

    private final Exec exec;

    private final DdlSync ddlSync;

    private final String workDir;

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
        //检查有没有已经解压的目录，
        //如果有就返回，如果没有就新建一个文件夹，并且解压pg_dump命令
        boolean isExist = checkDirAndCommand(workDir);
        if (!isExist) {
            decompression(workDir);
        }
    }

    private void decompression(String workDir) {
        ClassPathResource postgresResources = new ClassPathResource(PgDumpUtils.getResourcePath());
        try (InputStream inputStream = postgresResources.getInputStream()) {
            String workspace = PgDumpUtils.getUncompressDir(workDir);
            File outputFile = new File(workspace);
            if (!outputFile.exists()) {
                boolean mkdirs = outputFile.mkdirs();
                if (!mkdirs) {
                    log.warn("create workdir failed");
                }
            }
            TzxUtils.extractTarXz(inputStream, workspace);

            if (!OSUtils.isWindows()) {
                //设置pg_dump的权限
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString(RWXR_XR_X);
                Files.setPosixFilePermissions(Path.of(PgDumpUtils.getPgDumpPath(workDir)), perms);
            }
        } catch (IOException e) {
            log.error("Decompression package failed.", e);
        }
    }

    private boolean checkDirAndCommand(String workDir) {
        if (PgDumpUtils.existWorkspace(workDir)) {
            boolean exists = Files.exists(Path.of(PgDumpUtils.getPgDumpPath(workDir)));
            if (!exists) {
                //如果不存在，从新解压缩
                try {
                    Path directoryPath = Path.of(PgDumpUtils.getWorkspace(workDir));
                    try (Stream<Path> entries = Files.walk(directoryPath)) {
                        entries.sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(File::delete);
                    }
                } catch (IOException e) {
                    log.error("workDir exist but pg_dump not exist. delete UncompressDir failed.", e);
                }
                return false;
            }
            return true;
        }
        return false;
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
            workDir = PgDumpUtils.getDefaultDir();
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
