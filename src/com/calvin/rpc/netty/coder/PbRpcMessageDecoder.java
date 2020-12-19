package com.calvin.rpc.netty.coder;

import com.calvin.rpc.netty.message.NsHeader;
import com.calvin.rpc.netty.message.PbRpcMsg;
import com.calvin.rpc.util.ContextHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PbRpcMessageDecoder extends ByteToMessageDecoder {

    private static final Logger Log = LoggerFactory.getLogger(PbRpcMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //解决半包问题，此时Nshead还没有接收全，channel中留存的字节流不做处理
        if (in.readableBytes() < NsHeader.NSHEAD_LEN) {
            return;
        }

        in.markReaderIndex();

        byte[] bytes = new byte[NsHeader.NSHEAD_LEN];
        in.readBytes(bytes, 0, NsHeader.NSHEAD_LEN);

        NsHeader header = new NsHeader();
        header.wrap(bytes);

        //包头不匹配
        if (header.getHeader() != NsHeader.HEADER_FLAG) {
            in.resetReaderIndex();
            in.readShort();
            Log.error("Message header verify failed!");
            return;
        }

        // 解决半包问题，此时body还没有接收全，channel中留存的字节流不做处理，重置readerIndex
        if (in.readableBytes() < (int) header.getBodyLen()) {
            in.resetReaderIndex();
            return;
        }

        // 此时接受到了足够的一个包，开始处理
        in.markReaderIndex();

        byte[] bodyBytes = new byte[(int) header.getBodyLen()];
        in.readBytes(bodyBytes, 0, header.getBodyLen());
        PbRpcMsg msg = PbRpcMsg.of(header);
        msg.setData(bodyBytes);
        ContextHolder.putContext("_logId", header.getLogId());

        if (msg != null) {
            out.add(msg);
        }

    }
}
