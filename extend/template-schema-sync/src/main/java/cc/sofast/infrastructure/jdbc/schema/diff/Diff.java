package cc.sofast.infrastructure.jdbc.schema.diff;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;

import java.io.IOException;

/**
 * @author apple
 */
public interface Diff {

    /**
     * pg_dump
     *
     * @param source     原
     * @param target     目标
     * @param binFileDir pg_dump 目录
     * @return diff markdown
     * @throws IOException Exception
     */
    String diff(SchemaInfo source, SchemaInfo target, String binFileDir) throws IOException;
}
