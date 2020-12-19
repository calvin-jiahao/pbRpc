package com.calvin.rpc.netty.handler;

import com.calvin.rpc.core.Codec;
import com.calvin.rpc.core.ServiceDescriptor;
import com.calvin.rpc.core.ServiceLocator;
import com.calvin.rpc.core.impl.ProtobufCodec;
import com.calvin.rpc.netty.message.PbRpcMsg;
import com.calvin.rpc.util.ContextHolder;
import com.calvin.rpc.util.exception.ErrorCode;
import com.calvin.rpc.util.exception.PreCondition;
import com.google.protobuf.GeneratedMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.CodecException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.ServiceNotFoundException;
import java.lang.reflect.InvocationTargetException;

public class PbRpcServerHandler extends SimpleChannelInboundHandler<PbRpcMsg> {

    private static final Logger Log = LoggerFactory.getLogger(PbRpcServerHandler.class);

    /**
     * 可配置，默认使用protobuf来做body的序列化
     */
    private Codec codec = new ProtobufCodec();

    /**
     * 可配置，服务定位器
     */
    private ServiceLocator<Integer> serviceLocator;

    public PbRpcServerHandler(ServiceLocator<Integer> serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PbRpcMsg msg) throws Exception {
        PreCondition.checkArgument(msg != null, "PbRpc msg is null which should never happen");

        try {
            if (msg.getErrorCode() != null) {
                ctx.channel().writeAndFlush(PbRpcMsg.copyLiteOf(msg));
                return;
            }
            int serviceId = msg.getServiceId();
            ServiceDescriptor<Integer> descriptor = serviceLocator.getServiceDescriptor(serviceId);
            if (descriptor == null) {
                throw new ServiceNotFoundException(" serviceId=" + msg.getServiceId());
            }

            Object[] arg = (Object[]) codec.decode(null, msg.getData());
            Object ret = descriptor.getMethod().invoke(descriptor.getTarget(), arg);

            PbRpcMsg retMsg = new PbRpcMsg();
            retMsg.setLogId(msg.getLogId());
            retMsg.setServiceId(serviceId);

            if (ret != null) {
                byte[] response = codec.encode(null, ret);
                retMsg.setData(response);
            }

            ctx.channel().writeAndFlush(retMsg);

        } catch (ServiceNotFoundException e) {
            Log.error(ErrorCode.SERVICE_NOT_FOUND.getMessage() + e.getMessage(), e);
            ctx.channel().writeAndFlush(PbRpcMsg.copyLiteOf(msg).setErrorCode(ErrorCode.SERVICE_NOT_FOUND));
        } catch (CodecException e) {
            Log.error(ErrorCode.PROTOBUF_CODEC_ERROR.getMessage() + e.getMessage(), e);
            ctx.channel().writeAndFlush(PbRpcMsg.copyLiteOf(msg).setErrorCode(ErrorCode.PROTOBUF_CODEC_ERROR));
        } catch (InvocationTargetException e) {
            Log.error(ErrorCode.INVOCATION_TARGET_EXCEPTION.getMessage() + e.getMessage(), e);
            ctx.channel().writeAndFlush(PbRpcMsg.copyLiteOf(msg).setErrorCode(ErrorCode.INVOCATION_TARGET_EXCEPTION));
        } catch (Exception e) {
            Log.error(ErrorCode.UNEXPECTED_ERROR.getMessage() + e.getMessage(), e);
            ctx.channel().writeAndFlush(PbRpcMsg.copyLiteOf(msg).setErrorCode(ErrorCode.UNEXPECTED_ERROR));
        } finally {
            ContextHolder.clean();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.error("channelActive, ctx : " + ctx.channel());
    }
}
