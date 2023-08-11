package cc.sofast.infrastructure.customization.script;

public class EngineExecutorResult {


    /**
     * 执行状态
     */
    private ExecutionStatus executionStatus;

    /**
     * 返回内容
     */
    private Object context;

    /**
     * 异常信息
     */
    private Throwable exception;

    /**
     * 自定义异常描述
     */
    private String errorMessage;

    /**
     * 获取context为指定的类型
     */
    public <T> T context() {
        return (T) context;
    }

    private EngineExecutorResult(ExecutionStatus executionStatus, String errorMessage) {
        this.executionStatus = executionStatus;
        this.errorMessage = errorMessage;
    }

    private EngineExecutorResult(ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    private EngineExecutorResult(ExecutionStatus executionStatus, Throwable exception) {
        this.executionStatus = executionStatus;
        this.exception = exception;
    }

    private EngineExecutorResult(ExecutionStatus executionStatus, Object context) {
        this.executionStatus = executionStatus;
        this.context = context;
    }

    /**
     * 执行失败
     *
     * @param exception 异常信息
     * @return org.basis.enhance.groovy.entity.EngineExecutorResult<java.lang.Object>
     * @author wenpan 2022/9/18 12:54 下午
     */
    public static EngineExecutorResult failed(Throwable exception) {
        return new EngineExecutorResult(ExecutionStatus.FAILED, exception);
    }

    /**
     * 执行失败
     *
     * @param errorMessage 异常信息
     * @return org.basis.enhance.groovy.entity.EngineExecutorResult<java.lang.Object>
     * @author wenpan 2022/9/18 12:54 下午
     */
    public static EngineExecutorResult failed(String errorMessage) {
        return new EngineExecutorResult(ExecutionStatus.PARAM_ERROR, errorMessage);
    }

    /**
     * 执行成功
     *
     * @param context 内容
     * @return org.basis.enhance.groovy.entity.EngineExecutorResult<java.lang.Object>
     * @author wenpan 2022/9/18 12:55 下午
     */
    public static EngineExecutorResult success(Object context) {
        return success(ExecutionStatus.SUCCESS, context);
    }

    /**
     * 执行成功
     *
     * @param context 内容
     * @param status  执行状态
     * @return org.basis.enhance.groovy.entity.EngineExecutorResult<java.lang.Object>
     * @author wenpan 2022/9/18 12:55 下午
     */
    public static EngineExecutorResult success(ExecutionStatus status, Object context) {
        return new EngineExecutorResult(status, context);
    }
}
