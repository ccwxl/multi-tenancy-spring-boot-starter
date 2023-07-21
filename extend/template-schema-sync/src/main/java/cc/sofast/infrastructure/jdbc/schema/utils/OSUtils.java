package cc.sofast.infrastructure.jdbc.schema.utils;

import java.io.File;

public class OSUtils {

    public static final String DARWIN = "darwin";
    public static final String LINUX = "linux";
    public static final String WINDOWS = "windows";

    /**
     * 获取系统的架构
     *
     * @return DARWIN，LINUX，WINDOWS
     */
    public static String getOSArch() {
        if (isWindows()) {
            return WINDOWS;
        }

        if (isMacOs()) {
            return DARWIN;
        }

        return LINUX;
    }


    /**
     * 判断操作系统是否是 Windows
     *
     * @return true：操作系统是 Windows
     * false：其它操作系统
     */
    public static boolean isWindows() {
        String osName = getOsName();

        return osName != null && osName.startsWith("Windows");
    }

    /**
     * 判断操作系统是否是 MacOS
     *
     * @return true：操作系统是 MacOS
     * false：其它操作系统
     */
    public static boolean isMacOs() {
        String osName = getOsName();

        return osName != null && osName.startsWith("Mac");
    }

    /**
     * 判断操作系统是否是 Linux
     *
     * @return true：操作系统是 Linux
     * false：其它操作系统
     */
    public static boolean isLinux() {
        String osName = getOsName();

        return (osName != null && osName.startsWith("Linux")) || (!isWindows() && !isMacOs());
    }

    /**
     * 获取操作系统名称
     *
     * @return os.name 属性值
     */
    public static String getOsName() {
        return System.getProperty("os.name");
    }

    /**
     * 获取用户的工作目录，返回的是当前程序运行的工作目录
     *
     * @return 用户的工作目录
     */
    public static File getUserDir() {
        return new File(System.getProperty("user.dir"));
    }

    /**
     * 获取用户的home目录
     *
     * @return 用户home
     */
    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }
}
