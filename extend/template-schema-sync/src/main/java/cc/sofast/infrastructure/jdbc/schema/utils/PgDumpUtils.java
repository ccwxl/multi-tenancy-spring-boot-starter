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
     * @param workDir 目录
     * @return 目录
     */
    public static String getUncompressDir(String workDir) {
        if (StringUtils.hasLength(workDir)) {
            return workDir + "/";
        } else {
            return getDefaultDir();
        }
    }

    /**
     * 获取默认的工作目录
     *
     * @return 默认的工作目录
     */
    public static String getDefaultDir() {

        return OSUtils.getUserHome().getAbsoluteFile() + "/multitenancy";
    }

    /**
     * 工作目录 父目录+子目录
     *
     * @param workDir 工作目录
     * @return 工作目录 父目录+子目录
     */
    public static String getWorkspace(String workDir) {
        return getUncompressDir(workDir) + getFormatPath();
    }

    /**
     * 工作空间是否存在
     *
     * @param workDir 工作空间
     * @return 工作空间
     */
    public static boolean existWorkspace(String workDir) {
        String workspace = getWorkspace(workDir);
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
    public static String getResourcePath() {

        return "postgresql" + "/" + getFormatPath() + ".txz";
    }


    public static String getPgDumpPath(String workDir) {

        return getWorkspace(workDir) + "/bin/pg_dump";
    }
}
