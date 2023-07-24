package cc.sofast.infrastructure.jdbc.schema.utils;

import org.springframework.util.StringUtils;

import java.io.File;

public class PgDumpUtils {

    /**
     * 支持三种架构，linux，darwin，windows
     */
    public static final String dirFormat = "postgres-%s-x86_64";

    /**
     * 获取解压缩的父目录
     *
     * @param baseDir 目录
     * @return 目录
     */
    public static String getUncompressDir(String baseDir) {
        if (StringUtils.hasLength(baseDir)) {
            return baseDir + "/";
        } else {
            return getDefaultBaseDir();
        }
    }

    /**
     * 获取默认的工作目录
     *
     * @return 默认的工作目录
     */
    public static String getDefaultBaseDir() {

        return OSUtils.getUserHome().getAbsoluteFile() + "/multitenancy";
    }

    /**
     * 工作目录 父目录+子目录
     *
     * @param baseDir 工作目录
     * @return 工作目录 父目录+子目录
     */
    public static String getBaseDir(String baseDir) {
        return getUncompressDir(baseDir) + getFormatPath();
    }

    /**
     * 工作空间是否存在
     *
     * @param baseDir 工作空间
     * @return 工作空间
     */
    public static boolean existWorkspace(String baseDir) {
        String workspace = getBaseDir(baseDir);
        File directory = new File(workspace);
        return directory.exists() && directory.isDirectory();
    }

    /**
     * 格式化后的包全名称
     *
     * @return 压缩包的全名称
     */
    public static String getFormatPath() {

        return String.format(dirFormat, OSUtils.getOSArch());
    }

    /**
     * 资源
     *
     * @return txz包
     */
    public static String getPgTarPackagePath() {

        return "postgresql" + "/" + getFormatPath() + ".txz";
    }

    /**
     * 获取pg_dump 可执行文件的路径
     *
     * @param baseDir 目录
     * @return path
     */
    public static String getPgDumpBinFileDir(String baseDir) {
        return getBaseDir(baseDir) + "/bin/";
    }

    /**
     * 获取pg_dump 可执行文件的路径
     *
     * @param baseDir 目录
     * @return path
     */
    public static String getPgDumpBinPath(String baseDir) {
        if (OSUtils.isWindows()) {
            return getBaseDir(baseDir) + "/bin/pg_dump.exe";
        }
        return getBaseDir(baseDir) + "/bin/pg_dump";

    }
}
