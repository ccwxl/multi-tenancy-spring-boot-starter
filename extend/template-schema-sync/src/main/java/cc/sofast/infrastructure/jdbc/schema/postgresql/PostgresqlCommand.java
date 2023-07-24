package cc.sofast.infrastructure.jdbc.schema.postgresql;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;

public interface PostgresqlCommand {

    /**
     * 构造命令
     *
     * @param schema schema 信息
     * @param binDir pg_dump 二进制文件所在的目录
     * @return 构建好的命令
     */
    String builder(SchemaInfo schema, String binDir);

}
