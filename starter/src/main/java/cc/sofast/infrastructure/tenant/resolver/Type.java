package cc.sofast.infrastructure.tenant.resolver;

/**
 * @author apple
 * 解析方式
 */

public enum Type {

    /**
     * 根据HTTP REQUEST 解析
     */
    WEB,

    /**
     * 固定解析方式
     */
    FIXED,

    /**
     * 系统属性
     */
    PROPERTIES,
}
