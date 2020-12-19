package com.calvin.rpc.util;


import com.calvin.rpc.client.AbstractBaseTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class ScheduledTaskUtil {

    private static final Logger Log = LoggerFactory.getLogger(ScheduledTaskUtil.class);

    /**
     * 初始化1个线程
     */
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    /**
     * 计数器
     */
    private static AtomicInteger usageCount = new AtomicInteger(0);

    public ScheduledTaskUtil() {
    }

    /**
     * @param task                    Task to be scheduled
     * @param delayCheckMilliSeconds  Delay in milliseconds before task is executed
     * @param checkPeriodMilliSeconds Time in milliseconds between executions
     */
    public static synchronized void schedule(AbstractBaseTask task, int delayCheckMilliSeconds, int checkPeriodMilliSeconds) {
        if (executor == null) {
            return;
        }
        usageCount.incrementAndGet();
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(task, delayCheckMilliSeconds, checkPeriodMilliSeconds, TimeUnit.MILLISECONDS);
        task.setFuture(scheduledFuture);

        //任务新开线程
        if (usageCount.get() > executor.getCorePoolSize() && usageCount.get() < Runtime.getRuntime().availableProcessors()) {
            executor.setCorePoolSize(usageCount.get());
        }
        Log.error("--------add schedule info=" + task + ",taskcount=" + usageCount.get() + ",CorePoolSize=" + executor.getCorePoolSize());
    }

    /**
     * <pre>
     * task 计数器减1
     * </pre>
     */
    public static void decrementTaskCount() {
        usageCount.decrementAndGet();
    }

    public static synchronized void shutdown() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
    }
}
