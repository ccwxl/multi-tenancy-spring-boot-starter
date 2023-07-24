package cc.sofast.infrastructure.jdbc.schema;

import cc.sofast.infrastructure.jdbc.schema.diff.Diff;
import cc.sofast.infrastructure.jdbc.schema.diff.SchemaDiff;
import cc.sofast.infrastructure.jdbc.schema.exec.Exec;
import cc.sofast.infrastructure.jdbc.schema.exec.JdbcTemplateExec;
import cc.sofast.infrastructure.jdbc.schema.sync.TenantDDLSync;
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

    private final TenantDDLSync tenantDdlSync;

    private final String baseDir;

    public Schema() {
        this(new Builder());
    }

    public Schema(Builder builder) {
        this.diff = builder.diff;
        this.exec = builder.exec;
        this.tenantDdlSync = builder.tenantDdlSync;
        this.baseDir = builder.baseDir;
    }

    public void init() {
        //将txz文件解压到工作目录
        //检查有没有已经解压的目录，
        //如果有就返回，如果没有就新建一个文件夹，并且解压pg_dump命令
        boolean isExist = checkDirAndCommand(baseDir);
        if (!isExist) {
            decompression(baseDir);
        }
    }

    private void decompression(String baseDir) {
        ClassPathResource postgresResources = new ClassPathResource(PgDumpUtils.getPgTarPackagePath());
        try (InputStream inputStream = postgresResources.getInputStream()) {
            String workspace = PgDumpUtils.getUncompressDir(baseDir);
            File outputFile = new File(workspace);
            if (!outputFile.exists()) {
                boolean mkdirs = outputFile.mkdirs();
                if (!mkdirs) {
                    log.warn("create baseDir failed");
                }
            }
            TzxUtils.extractTarXz(inputStream, workspace);

            if (!OSUtils.isWindows()) {
                //设置pg_dump的权限
                Set<PosixFilePermission> perms = PosixFilePermissions.fromString(RWXR_XR_X);
                Files.setPosixFilePermissions(Path.of(PgDumpUtils.getPgDumpBinPath(baseDir)), perms);
            }
        } catch (IOException e) {
            log.error("Decompression package failed.", e);
        }
    }

    private boolean checkDirAndCommand(String baseDir) {
        if (PgDumpUtils.existWorkspace(baseDir)) {
            boolean exists = Files.exists(Path.of(PgDumpUtils.getPgDumpBinPath(baseDir)));
            if (!exists) {
                //如果不存在，从新解压缩
                try {
                    Path directoryPath = Path.of(PgDumpUtils.getBaseDir(baseDir));
                    try (Stream<Path> entries = Files.walk(directoryPath)) {
                        entries.sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(File::delete);
                    }
                } catch (IOException e) {
                    log.error("baseDir exist but pg_dump not exist. delete UncompressDir failed.", e);
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

        private TenantDDLSync tenantDdlSync;

        private String baseDir;

        public Builder(Schema schema) {
            this.diff = schema.diff;
            this.exec = schema.exec;
            this.tenantDdlSync = schema.tenantDdlSync;
            this.baseDir = schema.baseDir;
        }

        public Builder() {
            diff = new SchemaDiff();
            exec = new JdbcTemplateExec();
            tenantDdlSync = new TenantDDLSync(exec);
            baseDir = PgDumpUtils.getDefaultBaseDir();
        }

        public Builder diff(Diff diff) {
            this.diff = diff;
            return this;
        }

        public Builder baseDir(String baseDir) {
            this.baseDir = baseDir;
            return this;
        }

        public Builder exec(Exec exec) {
            this.exec = exec;
            return this;
        }

        public Builder ddlSync(TenantDDLSync tenantDdlSync) {
            this.tenantDdlSync = tenantDdlSync;
            return this;
        }

        public Schema build() {

            return new Schema(this);
        }
    }
}
