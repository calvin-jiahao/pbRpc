package com.calvin.rpc.netty.coder;

import com.calvin.rpc.netty.message.NsHeader;
import com.calvin.rpc.netty.message.PbRpcMsg;
import com.calvin.rpc.util.ByteUtil;
import com.calvin.rpc.util.IdGenerator;
import com.calvin.rpc.util.exception.ErrorCode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class PbRpcMessageEncoder extends MessageToMessageEncoder<PbRpcMsg> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, PbRpcMsg pbRpcMsg, List<Object> list) throws Exception {
        byte[] bodyBytes = ByteUtil.getNonNUllBytes(pbRpcMsg.getData());
        NsHeader header = constructHeader(pbRpcMsg.getServiceId(), pbRpcMsg.getErrorCode(), pbRpcMsg.getLogId(), (short) bodyBytes.length);
        byte[] headerBytes = header.toBytes();
        ByteBuf buf = Unpooled.copiedBuffer(headerBytes, bodyBytes);
        list.add(buf);
    }

    public NsHeader constructHeader(int serviceId, ErrorCode errorCode, long logId, short bodyLen) {
        NsHeader header = new NsHeader();
        header.setServiceId(serviceId);
        //客户端logId为0
        header.setLogId(logId != 0 ? logId : IdGenerator.getUUID());
        if (errorCode != null) {
            header.setResultCode(errorCode.getValue());
        }
        header.setBodyLen(bodyLen);
        return header;
    }

}
