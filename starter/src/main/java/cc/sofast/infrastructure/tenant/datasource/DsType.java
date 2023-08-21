package cc.sofast.infrastructure.tenant.datasource;

/**
 * 数据源类型
 *
 * @author apple
 */
public enum DsType {

    /**
     * 独享
     */
    EXCLUSIVE,

    /**
     * 共享
     */
    SHARE,

    /**
     * 共享,RLS 行级别隔离
     */
    RLS,
}
