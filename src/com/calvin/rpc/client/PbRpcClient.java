package com.calvin.rpc.client;

import com.calvin.rpc.client.callback.CallFuture;
import com.calvin.rpc.netty.message.PbRpcMsg;
import com.google.protobuf.GeneratedMessage;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.lang.reflect.Method;


public interface PbRpcClient {

    /**
     * 连接组标示
     */
    public static final AttributeKey<PbRpcClient> LINKED_CLIENT = AttributeKey.valueOf("LINKED_CLIENT");

    /**
     * 连接远程服务器
     *
     * @return ChannelFuture netty nio方式连接后的future回调
     */
    Channel connect();

    /**
     * 重连
     */
    boolean reConnect();

    /**
     * 关闭客户端的远程连接
     */
    void shutdown();

    /**
     * 异步调用
     *
     * @param responseClazz 调用待返回的对象类型，T为对象的<tt>Class</tt>类型
     * @param pbRpcMsg pbRpcMsg 调用的消息对象
     * @return future回调，带有泛型T标示待返回对象类型
     */
    <T> CallFuture<T> asyncTransport(Class<T> responseClazz, PbRpcMsg pbRpcMsg) ;


    /**
     * 同步调用
     *
     * @param responseClazz 调用待返回的对象类型，T为对象的<tt>Class</tt>类型
     * @param pbRpcMsg PbRpc调用的消息对象
     * @return 调用返回的protobuf对象
     */
    <T> T syncTransport(Class<T> responseClazz, PbRpcMsg pbRpcMsg);


    /**
     * 返回描述信息
     *
     * @return 客户端信息
     */
    String getInfo();

    /**
     * <pre>
     * 服务的标识ID
     * </pre>
     *
     * @return
     */
    int getServerId(Method method);

    /**
     * <pre>
     * 注册要代理的对象
     * </pre>
     *
     * @param proxyClass 要代理的类型
     * @param proxyBean 要代理的对象
     * @return
     */
    <T> T registerProxy(Class<T> proxyClass, Object proxyBean);

    /**
     * <pre>
     * 获取代理对象
     * </pre>
     *
     * @param proxyClass
     * @return
     */
    <T> T getProxyObject(Class<T> proxyClass);

}
