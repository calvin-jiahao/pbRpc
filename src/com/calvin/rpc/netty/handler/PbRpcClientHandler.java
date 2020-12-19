package com.calvin.rpc.netty.handler;

import com.calvin.rpc.client.FixedChannelPbrpcClient;
import com.calvin.rpc.client.PbRpcClient;
import com.calvin.rpc.client.callback.Callback;
import com.calvin.rpc.client.callback.CallbackContext;
import com.calvin.rpc.client.callback.CallbackPool;
import com.calvin.rpc.core.Codec;
import com.calvin.rpc.core.impl.ProtobufCodec;
import com.calvin.rpc.netty.message.PbRpcMsg;
import com.calvin.rpc.util.ContextHolder;
import com.calvin.rpc.util.exception.PreCondition;
import com.calvin.rpc.util.exception.client.ExceptionUtil;
import com.google.protobuf.GeneratedMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.CodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PbRpcClientHandler extends SimpleChannelInboundHandler<PbRpcMsg> {

    private static final Logger Log = LoggerFactory.getLogger(PbRpcClientHandler.class);

    /**
     * 可配置，默认使用protobuf来做body的序列化
     */
    private Codec codec = new ProtobufCodec();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PbRpcMsg pbRpcMsg) throws Exception {
        PreCondition.checkArgument(pbRpcMsg != null, "PbRpc msg is null which should never happen");

        try {
            int logId = (int) pbRpcMsg.getLogId();
            CallbackContext context = CallbackPool.getContext(logId);
            if (context == null) {
                Log.warn("Receive msg from server but no context found, logId=" + logId);
                return;
            }
//            Callback<GeneratedMessage> callback = CallbackPool.get(logId);
            Callback<Object> callback = (Callback<Object>) context.getCallback();
            if (pbRpcMsg.getErrorCode() != null) {
                callback.handleError(ExceptionUtil.buildFromErrorCode(pbRpcMsg.getErrorCode()));
            } else {
                Object msg = codec.decode(CallbackPool.getResClass(logId), pbRpcMsg.getData());
                callback.handleResult(msg);
            }

        } catch (CodecException e) {
            Log.warn("pbRpc client read data codec error ", e);
        } finally {
            CallbackPool.remove((int) pbRpcMsg.getLogId());
            ContextHolder.clean();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.info("连接异常断开 : " + ctx.channel().remoteAddress());
        PbRpcClient client = ctx.channel().attr(PbRpcClient.LINKED_CLIENT).get();
        if (client != null && client instanceof FixedChannelPbrpcClient) {
            ((FixedChannelPbrpcClient) client).startReconnectTask();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Log.error("client socket exception", cause);
        ctx.close();
    }
}
