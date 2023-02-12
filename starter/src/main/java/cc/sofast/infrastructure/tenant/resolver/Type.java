package cc.sofast.infrastructure.tenant.resolver;

/**
 * @author apple
 * 解析方式
 */

public enum Type {

    /**
     * 根据http request 解析
     */
    web,

    /**
     * 固定解析方式
     */
    fixed,

    /**
     * 系统属性
     */
    properties,
}
