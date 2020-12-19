package com.calvin.rpc.client.callback;

import com.google.protobuf.GeneratedMessage;

/**
 * 回调上下文，用于定位回调，计算超时、关闭channel等辅助功能
 */
public class CallbackContext {

    /**
     * 用于标示某个回调的id，用NsHead头中的logId来标示
     */
    private final int logId;

    /**
     * 调用起始时间
     */
    private final long startTime;

    /**
     * 调用结束时间
     */
    private final int timeout;

    /**
     * 期望服务返回的pb类型
     */
    private final Class<?> resClazz;

    /**
     * 回调
     */
    private final Callback<?> callback;

    /**
     * Creates a new instance of CallbackContext.
     *
     * @param logId
     * @param startTime
     * @param timeout
     * @param resClazz
     * @param callback
     */
    public CallbackContext(int logId, long startTime, int timeout, Class<?> resClazz, Callback<?> callback) {
        super();
        this.logId = logId;
        this.startTime = startTime;
        this.timeout = timeout;
        this.resClazz = resClazz;
        this.callback = callback;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getLogId() {
        return logId;
    }

    public Class<?> getResClazz() {
        return resClazz;
    }

    public Callback<?> getCallback() {
        return callback;
    }
}
