package cc.sofast.infrastructure.jdbc.schema.pgdump;

import cc.sofast.infrastructure.jdbc.schema.SchemaInfo;
import cc.sofast.infrastructure.jdbc.schema.model.PgDumpResponse;
import cc.sofast.infrastructure.jdbc.schema.model.TaskRunStatus;

/**
 * @author apple
 * apache io CompositeFileComparator
 */
public abstract class AbstractPgDump implements PgDump {

    public PgDumpResponse run(SchemaInfo option) {
        //构建一个 pg_dump  命令

        //启动一个线程：将 pg_dump 输出放入到一个
        //启动另一个线程：读取 block_queue

        //超时处理?

        return new PgDumpResponse();
    }

    public void buildPgDumpCommand(SchemaInfo option) {

    }

    @Override
    public PgDumpResponse dump(SchemaInfo option) {
        PgDumpResponse response = new PgDumpResponse();
        try {
            response = run(option);
        } catch (Exception e) {
            response.setStatus(TaskRunStatus.FAIL);
        }
        return response;
    }
}
