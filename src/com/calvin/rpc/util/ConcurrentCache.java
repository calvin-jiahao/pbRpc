package com.calvin.rpc.util;

import java.util.concurrent.*;

public class ConcurrentCache<K, V> implements Computable<K, V> {

    /**
     * 并发安全Map
     */
    private final ConcurrentMap<K, Future<V>> concurrentMap;

    /**
     * Creates a new instance of ConcurrentCache.
     */
    public ConcurrentCache() {
        concurrentMap = new ConcurrentHashMap<>();
    }

    /**
     * 静态方法返回计算接口
     *
     * @return
     */
    public static <K, V> Computable<K, V> createComputable() {
        return new ConcurrentCache<K, V>();
    }

    /**
     * 通过关键字获取数据，如果存在则直接返回，如果不存在，则通过计算<code>callable</code>来生成
     *
     * @param key 查找关键字
     * @param callable # @see Callable
     * @return 计算结果
     */
    @Override
    public V get(K key, Callable<V> callable) {
        Future<V> future = concurrentMap.get(key);
        if (future == null) {
            FutureTask<V> futureTask = new FutureTask<V>(callable);
            future = concurrentMap.putIfAbsent(key, futureTask);
            if (future == null) {
                future = futureTask;
                futureTask.run();
            }
        }

        try {
            // 此时阻塞
            return future.get();
        } catch (Exception e) {
            concurrentMap.remove(key);
            return null;
        }

    }
}
