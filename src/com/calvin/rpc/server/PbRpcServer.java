package com.calvin.rpc.server;

import com.calvin.rpc.core.ServiceLocator;
import com.calvin.rpc.core.impl.IdKeyServiceLocator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PbRpcServer {
    private static final Logger Log = LoggerFactory.getLogger(PbRpcServer.class);

    /**
     * 服务端口
     */
    private int port;

    /**
     * netty服务启动对象
     */
    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private PbRpcServerConfig config;

    private ServiceLocator<Integer> serviceLocator = new IdKeyServiceLocator();

    public PbRpcServer(int port) {
        this(null, port);
    }

    public PbRpcServer(PbRpcServerConfig config, int port) {
        this.config = config;
        this.port = port;
        initNettyService();
    }

    private void initNettyService() {
        if (config == null) {
            config = new PbRpcServerConfig();
        }
        try {
            boolean isEpoll = Epoll.isAvailable();
            if (isEpoll) {
                bossGroup = new EpollEventLoopGroup(config.getBossGroupCount());
                workerGroup = new EpollEventLoopGroup(config.getWorkerGroupCount());
            } else {
                bossGroup = new NioEventLoopGroup(config.getBossGroupCount());
                workerGroup = new NioEventLoopGroup(config.getWorkerGroupCount());
            }
            bootstrap = new ServerBootstrap();
            bootstrap.channel(isEpoll ? EpollServerSocketChannel.class : NioSctpServerChannel.class);

            bootstrap.option(ChannelOption.SO_REUSEADDR, config.isSoReuseaddr());
            bootstrap.option(ChannelOption.SO_RCVBUF, config.getSoRcvbuf());
            bootstrap.option(ChannelOption.SO_BACKLOG, config.getSoBacklog());
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, config.isSoKeepalive());
            bootstrap.childOption(ChannelOption.SO_LINGER, config.getSoLinger());
            bootstrap.childOption(ChannelOption.TCP_NODELAY, config.isTcpNodelay());
            bootstrap.childOption(ChannelOption.SO_SNDBUF, config.getSoSndbuf());
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 64, 1024 * 128));

            bootstrap.group(workerGroup, bossGroup).childHandler(new ServerInitializer(serviceLocator));

            Log.info("NettyServer init, port = " + port + ", isEpoll = " + isEpoll);

        } catch (Exception e) {
            Log.error("init netty exception.port = " + port, e);
        }
    }

    /**
     * 启动服务
     *
     * @return
     */
    public boolean start() {
        Log.info("PbRpc server is about to start on port " + port);
        try {
            bootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            Log.error("Server start failed to start, " + e.getMessage(), e);
            return false;
        }
        Log.info("Server started, port=" + port);
        return true;
    }

    /**
     * 注册某个bean到服务中暴露
     *
     * server中注册服务impl
     *
     * @param serviceBean
     */
    public void register(Object serviceBean) {
        serviceLocator.registerService(serviceBean);
    }

    /**
     * 关闭服务
     */
    public void shutdown() {
        Log.info("PbRpc server stop");
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }

    }


}
