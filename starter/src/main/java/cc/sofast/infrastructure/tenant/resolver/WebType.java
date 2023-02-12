package cc.sofast.infrastructure.tenant.resolver;

/**
 * @author apple
 * tenant web 下解析方式
 */
public enum WebType {

    /**
     * 请求头
     */
    HEADER,

    /**
     * cookie
     */
    COOKIE,

    /**
     * 请求路径
     */
    PATH,

    /**
     * 子域名
     */
    SUBDOMAIN
}
