package com.calvin.rpc.client;

import com.calvin.rpc.client.callback.CallFuture;
import com.calvin.rpc.client.callback.CallbackPool;
import com.calvin.rpc.core.impl.ServerInvokeHandler;
import com.calvin.rpc.netty.message.PbRpcMsg;
import com.calvin.rpc.util.IdGenerator;
import com.calvin.rpc.util.PbRpcConstants;
import com.calvin.rpc.util.ScheduledTaskUtil;
import com.calvin.rpc.util.exception.PbRpcException;
import com.calvin.rpc.util.exception.client.PbRpcConnectionException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;


public class FixedChannelPbrpcClient implements PbRpcClient {

    private static final Logger Log = LoggerFactory.getLogger(FixedChannelPbrpcClient.class);

    /**
     * 远程服务ip地址
     */
    private String ip;

    /**
     * 远程服务端口
     */
    private int port;

    /**
     * 客户端连接超时，单位毫秒
     */
    private int connTimeout;

    /**
     * 客户端调用超时，单位毫秒
     */
    private int readTimeout;

    /**
     * 客户端配置
     */
    private PbRpcClientConfig pbRpcClientConfig;

    private Bootstrap bootstrap;

    private EventLoopGroup eventLoopGroup;

    private Channel[] channels;

    private Map<Method, Integer> serverIdMap = new HashMap<>();

    @SuppressWarnings("rawtypes")
    private Map<Class, Object> proxyMap = new HashMap<>();

    /**
     * 检测客户端超时任务是否已启动，如果启动了则不会重复启动（全局）
     */
    private static volatile AtomicBoolean isTimeoutTaskStarted = new AtomicBoolean(false);

    /**
     * 检测断线重连的任务是否已启动(对象私有)
     */
    private volatile AtomicBoolean isReconnectTaskStarted = new AtomicBoolean(false);

    /**
     * 启动检测客户端调用超时的检测器
     */
    public void startTimeoutTask() {
        // 保证所有client线程共用一个timeout检测器
        if (isTimeoutTaskStarted.compareAndSet(false, true)) {
            Log.info("Start timeout task, delayStartTime=" + PbRpcConstants.CLIENT_TIMEOUT_EVICTOR_DELAY_START_TIME + ", checkInterval=" + PbRpcConstants.CLIENT_TIMEOUT_EVICTOR_CHECK_INTERVAL);
            ScheduledTaskUtil.schedule(new TimeoutTask(), PbRpcConstants.CLIENT_TIMEOUT_EVICTOR_DELAY_START_TIME, PbRpcConstants.CLIENT_TIMEOUT_EVICTOR_CHECK_INTERVAL);
        }
    }

    /**
     * <pre>
     * 启动重连检测器，同一时间只能有一个重连任务
     * </pre>
     */
    public void startReconnectTask() {
        if (isReconnectTaskStarted.compareAndSet(false, true)) {
            Log.info("Start reconnect task, delayStartTime=" + PbRpcConstants.CLIENT_RECONNECT_TASK_DELAY_START_TIME + ", checkInterval=" + PbRpcConstants.CLIENT_RECONNECT_TASK_CHECK_INTERVAL);
            ScheduledTaskUtil.schedule(new ReConnectTask(this), PbRpcConstants.CLIENT_RECONNECT_TASK_DELAY_START_TIME, PbRpcConstants.CLIENT_RECONNECT_TASK_CHECK_INTERVAL);
        } else {
            Log.equals("------------ReconnectTask is runing!----------");
        }
    }

    /**
     * 构造客户端连接
     *
     * @param ip
     * @param port
     * @param connTimeout
     * @param readTimeout
     */
    public FixedChannelPbrpcClient(String ip, int port, int connTimeout, int readTimeout, int linkCount) {
        this(null, ip, port, connTimeout, readTimeout, linkCount);
    }

    /**
     * Creates a new instance of ShortLiveConnectionPbrpcClient.
     *
     * @param pbRpcClientConfig
     * @param ip
     * @param port
     * @param connTimeout
     * @param readTimeout
     */
    public FixedChannelPbrpcClient(PbRpcClientConfig pbRpcClientConfig, String ip, int port, int connTimeout, int readTimeout, int linkCount) {
        if (pbRpcClientConfig == null) {
            this.pbRpcClientConfig = new PbRpcClientConfig();
        }
        this.channels = new Channel[linkCount];
        this.ip = ip;
        this.port = port;
        this.connTimeout = connTimeout;
        this.readTimeout = readTimeout;

        boolean isEpoll = Epoll.isAvailable();

        if (isEpoll) {
            eventLoopGroup = new EpollEventLoopGroup();
        } else {
            eventLoopGroup = new NioEventLoopGroup();
        }

        bootstrap = new Bootstrap();
        bootstrap.channel(isEpoll ? EpollSocketChannel.class : NioSocketChannel.class);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.connTimeout);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, this.pbRpcClientConfig.isSoKeepalive());
        bootstrap.option(ChannelOption.SO_REUSEADDR, this.pbRpcClientConfig.isSoReuseaddr());
        bootstrap.option(ChannelOption.TCP_NODELAY, this.pbRpcClientConfig.isTcpNodelay());
        bootstrap.option(ChannelOption.SO_RCVBUF, this.pbRpcClientConfig.getSoRcvbuf());
        bootstrap.option(ChannelOption.SO_SNDBUF, this.pbRpcClientConfig.getSoSndbuf());

        ChannelInitializer<SocketChannel> initializer = new ClientInitializer();
        bootstrap.group(eventLoopGroup).handler(initializer);

        startTimeoutTask();

        if (!reConnect()) {
            startReconnectTask();
        }
    }

    @Override
    public Channel connect() {
        try {
            ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port)).syncUninterruptibly();

            if (future.isSuccess() && future.channel().isActive()) {
                Channel channel = future.channel();
                channel.attr(PbRpcClient.LINKED_CLIENT).set(this);
                return channel;
            } else {
                Log.error("连接完成但是连接不可用, isSuccess : " + future.isSuccess() + ", isAvtive : " + future.channel().isActive());
                return null;
            }
        } catch (Exception e) {
            Log.error("连接异常，info : " + getInfo(), e);
            return null;
        }
    }

    /**
     * <pre>
     * 重连服务器，检查异常的连接并进行重连
     * </pre>
     *
     * @return
     */
    @Override
    public boolean reConnect() {
        // 遍历连接的所有服务器
        int size = (channels != null) ? channels.length : 0;
        for (int i = 0; i < size; i++) {
            // 检测尚未连接上的服务器和无法连接的服务器
            Channel channel = channels[i];
            // 判断是否激活
            if (isValid(channel)) {
                continue; // 连接正常, 跳过.
            }
            // 进行连接
            try {
                Channel newChannel = connect();
                if (newChannel == null) {
                    return false; // 连接失败
                }
                channels[i] = newChannel; // 连接成功设置
            } catch (Exception e) {
                Log.error("重连服务器异常, info : " + getInfo(), e);
                return false;
            }
        }
        Log.error("-------------reConnect success---------------");
        // 重置重连状态
        isReconnectTaskStarted.compareAndSet(true, false);
        return true;
    }

    /**
     * <pre>
     * 判断连接是否有效
     * </pre>
     *
     * @param channel
     * @return
     */
    public boolean isValid(Channel channel) {
        return channel != null && channel.isActive();
    }

    @Override
    public void shutdown() {
        // 关闭定时任务
        ScheduledTaskUtil.shutdown();

        if (eventLoopGroup != null) {
            try {
                eventLoopGroup.shutdownGracefully().get();
            } catch (InterruptedException e) {
                Log.error("eventLoopGroup shutdownGracefully error", e);
            } catch (ExecutionException e) {
                Log.error("eventLoopGroup shutdownGracefully error", e);
            }
        }

        if (serverIdMap != null) {
            serverIdMap.clear();
            serverIdMap = null;
        }

        if (proxyMap != null) {
            proxyMap.clear();
            ;
            proxyMap = null;
        }
    }

    @Override
    public <T> CallFuture<T> asyncTransport(Class<T> responseClazz, PbRpcMsg pbRpcMsg) {
        try {
            return doAsyncTransport(responseClazz, pbRpcMsg);
        } catch (Exception e) {
            Log.error("Failed to transport to " + getInfo() + " due to " + e.getMessage(), e);
            throw new PbRpcException(e);
        }
    }

    /**
     * 同步调用
     *
     * @param responseClazz
     * @param pbRpcMsg
     * @return
     */
    @Override
    public <T> T syncTransport(Class<T> responseClazz, PbRpcMsg pbRpcMsg) {
        try {
            return asyncTransport(responseClazz, pbRpcMsg).get();
        } catch (InterruptedException e) {
            throw new PbRpcConnectionException("Transport failed on " + getInfo() + ", " + e.getMessage(), e);
        } catch (ExecutionException e) {
            throw new PbRpcConnectionException("Transport failed on " + getInfo() + ", " + e.getMessage(), e);
        }
    }

    public <T> CallFuture<T> doAsyncTransport(Class<T> responseClazz, PbRpcMsg pbRpcMsg) {
        int uuid = IdGenerator.getUUID();
        Channel channel = getChannel(uuid);

        if (isValid(channel)) {
            pbRpcMsg.setLogId(uuid);
            CallFuture<T> future = CallFuture.newInstance();
            CallbackPool.put(uuid, this.readTimeout, responseClazz, future);

            channel.writeAndFlush(pbRpcMsg);
            Log.debug("Send message " + pbRpcMsg + " done");
            return future;
        } else {
            Log.error("Socket channel is not well established, so failed to transport on " + getInfo());
            throw new PbRpcConnectionException("Socket channel is not well established,so failed to transport on " + getInfo());
        }
    }

    protected Channel getChannel(int id) {
        return channels[id % channels.length];
    }

    @Override
    public String getInfo() {
        return ip + ":" + port + ", connTimeout=" + this.connTimeout + ", readTimeout=" + this.readTimeout;
    }

    @Override
    public int getServerId(Method method) {
        return serverIdMap.get(method);
    }

    /**
     * 客户端注册动态代理对象
     *
     * @param proxyClass 要代理的类型
     * @param proxyBean  要代理的对象
     * @param <T>
     * @return
     */
    @Override
    public <T> T registerProxy(Class<T> proxyClass, Object proxyBean) {
        Method[] methods = proxyBean.getClass().getInterfaces()[0].getMethods();
        for (Method ms : methods) {
            serverIdMap.put(ms, ms.toString().hashCode());
        }
        T proxy = (T) Proxy.newProxyInstance(proxyBean.getClass().getClassLoader(), proxyBean.getClass().getInterfaces(), new ServerInvokeHandler(this));
        proxyMap.put(proxyClass, proxy);
        return proxy;
    }

    @Override
    public <T> T getProxyObject(Class<T> proxyClass) {
        return (T) proxyMap.get(proxyClass);
    }

}
