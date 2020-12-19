package com.calvin.rpc.client;

import com.calvin.rpc.netty.coder.PbRpcMessageDecoder;
import com.calvin.rpc.netty.coder.PbRpcMessageEncoder;
import com.calvin.rpc.netty.handler.PbRpcClientHandler;
import com.calvin.rpc.netty.handler.PbRpcServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("decoder", new PbRpcMessageDecoder());
        ch.pipeline().addLast("encoder", new PbRpcMessageEncoder());
        ch.pipeline().addLast("handler", new PbRpcClientHandler());
    }
}
