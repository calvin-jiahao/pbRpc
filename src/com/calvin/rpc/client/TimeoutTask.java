package com.calvin.rpc.client;

import com.calvin.rpc.client.callback.CallbackContext;
import com.calvin.rpc.client.callback.CallbackPool;
import com.calvin.rpc.util.exception.client.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TimeoutTask extends AbstractBaseTask {

    private static final Logger Log = LoggerFactory.getLogger(TimeoutTask.class);

    /**
     * 运行超时检测器
     *
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        try {
            detectTimeout();
        } catch (Exception e) {
            Log.warn("Exception occurred when detecting timeout callbacks", e);
        }
    }

    /**
     * 检测超时
     */
    private synchronized void detectTimeout() {
        long startTime = System.nanoTime();

        try {
            int totalScanned = 0;
            int timeoutCount = 0;

            ConcurrentHashMap<Integer, CallbackContext> contextMap = CallbackPool.getCALLBACK_MAP();
            if (contextMap != null && contextMap.size() > 0) {
                List<CallbackContext> contexts = new ArrayList<>(contextMap.values());
                for (CallbackContext context : contexts) {
                    if (context == null) {
                        continue;
                    }
                    if (System.currentTimeMillis() - context.getStartTime() > context.getTimeout()) {
                        context.getCallback().handleError(new TimeoutException("Client call timeout, request logId=" + context.getLogId()));
                        CallbackPool.remove(context.getLogId());
                        timeoutCount++;
                    }
                }
                totalScanned = contexts.size();
            }
        } catch (Exception e) {
            Log.warn("Exception occurred when detecting timeout callbacks", e);
        }
    }
}
