package cc.sofast.infrastructure.jdbc.schema.model;

import lombok.Data;

/**
 * @author apple
 */
@Data
public class PgDumpResponse {

    /**
     * dump 结果
     */
    private String result;

    /**
     * 进程id
     */
    private int processId;

    /**
     * cancel
     */
    private volatile boolean cancel = false;

    /**
     * exit code
     */
    private volatile int exitStatusCode = -1;

    /**
     * dump任务状态
     */
    private TaskRunStatus status;

}
