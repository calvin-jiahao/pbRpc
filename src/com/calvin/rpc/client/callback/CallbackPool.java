package com.calvin.rpc.client.callback;

import com.calvin.rpc.netty.message.NsHeader;
import com.google.protobuf.GeneratedMessage;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端回调池，用于保存调用发送请求出去的{@link CallbackContext}上下文，用于nio异步通信收到服务端响应后回调成功或者失败
 */
public class CallbackPool {

    /**
     * Map默认键数量，全局唯一静态{@link CallbackContext}的<code>ConcurrentHashMap</code>初始化参数
     */
    private static final int INITIAL_CAPACITY = 128 * 4 / 3;

    /**
     * Map的扩容装载因子，全局唯一静态{@link CallbackContext}的<code>ConcurrentHashMap</code>初始化参数
     */
    private static final float LOAD_FACTOR = 0.75f;

    /**
     * Map的并发度，也就是segament数量，读不锁写锁，全局唯一静态{@link CallbackContext}的<code>ConcurrentHashMap</code>初始化参数
     */
    private static final int CONCURRENCY_LEVEL = 16;

    /**
     * 保存{@link CallbackContext}的Map，键为调用的唯一标示，是{@link NsHeader}头中的<tt>logId</tt>
     */
    private static ConcurrentHashMap<Integer, CallbackContext> CALLBACK_MAP = new ConcurrentHashMap<>(INITIAL_CAPACITY, LOAD_FACTOR, CONCURRENCY_LEVEL);

    /**
     * 根据id标示获取上下文
     *
     * @param logId
     * @return
     */
    public static CallbackContext getContext(int logId) {
        CallbackContext callbackContext = CALLBACK_MAP.get(logId);
        return callbackContext == null ? null : callbackContext;
    }

    /**
     * 根据id标示获取上下文内的回调
     *
     * @param logId
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Callback<T> get(int logId) {
        CallbackContext callbackContext = CALLBACK_MAP.get(logId);
        return callbackContext == null ? null : (Callback<T>) callbackContext.getCallback();
    }

    /**
     * 根据id获取上下文内的待返回对象类型
     *
     * @param logId
     * @return
     */
    public static Class<?> getResClass(int logId) {
        CallbackContext callbackContext = CALLBACK_MAP.get(logId);
        return callbackContext == null ? null : callbackContext.getResClazz();
    }

    /**
     * 放入回调上下文
     *
     * @param logId    logId
     * @param timeout  客户端调用超时
     * @param resClazz 客户端要返回的class类型
     * @param callback 客户端句柄callback
     */
    public static <T> void put(int logId, int timeout, Class<T> resClazz, Callback<T> callback) {
        CALLBACK_MAP.putIfAbsent(logId, new CallbackContext(logId, System.currentTimeMillis(), timeout, resClazz, callback));
    }

    /**
     * 移除回调上下文
     *
     * @param logId
     */
    public static void remove(int logId) {
        CALLBACK_MAP.remove(logId);
    }

    /**
     * 清理Map
     */
    public static void clear() {
        CALLBACK_MAP.clear();
    }

    /**
     * 获取保存回调上下文的Map
     *
     * @return
     */
    public static ConcurrentHashMap<Integer, CallbackContext> getCALLBACK_MAP() {
        return CALLBACK_MAP;
    }
}
