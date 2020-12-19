package com.calvin.rpc.server;

import com.calvin.rpc.core.ServiceLocator;
import com.calvin.rpc.netty.coder.PbRpcMessageDecoder;
import com.calvin.rpc.netty.coder.PbRpcMessageEncoder;
import com.calvin.rpc.netty.handler.PbRpcServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceLocator<Integer> serviceLocator;

    public ServerInitializer(ServiceLocator<Integer> serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("decoder", new PbRpcMessageDecoder());
        ch.pipeline().addLast("encoder", new PbRpcMessageEncoder());
        ch.pipeline().addLast("handler", new PbRpcServerHandler(serviceLocator));
    }
}
