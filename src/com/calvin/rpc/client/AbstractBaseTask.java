package com.calvin.rpc.client;

import com.calvin.rpc.util.ScheduledTaskUtil;

import java.util.concurrent.ScheduledFuture;

public abstract class AbstractBaseTask implements Runnable {

    protected ScheduledFuture<?> future = null;

    public ScheduledFuture<?> getFuture() {
        return future;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    /**
     * <pre>
     * 取消定时任务
     * </pre>
     */
    public void cancel() {
        if (this.future != null) {
            this.future.cancel(true);
            ScheduledTaskUtil.decrementTaskCount();
        }
    }


}
