package com.calvin.rpc.netty.message;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 协议头部信息，长度为16个字节，详细信息如下：
 *
 * <pre>
 *       Byte/     0       |       1       |       2       |       3       |
 *          /              |               |               |               |
 *         |0 1 2 3 4 5 6 7|0 1 2 3 4 5 6 7|0 1 2 3 4 5 6 7|0 1 2 3 4 5 6 7|
 *         +---------------+---------------+---------------+---------------+
 *        0| flags                         | serviceId                     |
 *         +---------------+---------------+---------------+---------------+
 *        4| logId                                                         |
 *         +															   +
 *        8|															   |
 *         +---------------+---------------+---------------+---------------+
 *       12| body length                   | resultCode                    |
 *         +---------------+---------------+---------------+---------------+
 *         Total 16 bytes
 * </pre>
 */
public class NsHeader implements Header {

    /**
     * NsHead头大小
     */
    public static final int NSHEAD_LEN = 18;

    public static final short HEADER_FLAG = 0x555f;

    /**
     * 固定标识
     */
    private short header = HEADER_FLAG;

    /**
     * 对应一个方法的唯一id，用于服务的路由
     */
    private int serviceId;

    /**
     * 随机生成的追踪id，一般客户端调用时候赋值，服务端的响应会回写该id，用于上下文一致性的需求，类似于traceId
     */
    private long logId;

    /**
     * 数据的总字节长度
     */
    private short bodyLen;

    /**
     * 返回码
     */
    private short resultCode;

    public NsHeader(){}

    public NsHeader(byte[] input) {
        wrap(input);
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    @Override
    public short getBodyLen() {
        return bodyLen;
    }

    @Override
    public void setBodyLen(short bodyLen) {
        this.bodyLen = bodyLen;
    }

    public short getHeader() {
        return header;
    }

    public void setHeader(short header) {
        this.header = header;
    }

    public short getResultCode() {
        return resultCode;
    }

    public void setResultCode(short resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public int getFixedHeaderLen() {
        return NSHEAD_LEN;
    }

    /**
     * 从字节流还原Nshead头
     *
     * @param input
     */
    @Override
    public void wrap(byte[] input) {
        ByteBuffer buffer = ByteBuffer.allocate(NSHEAD_LEN);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(input);
        buffer.flip();
        header = buffer.getShort();
        serviceId = buffer.getShort();
        logId = buffer.getLong();
        bodyLen = buffer.getShort();
        resultCode = buffer.getShort();
    }

    /**
     * 转为字节流 <br/>
     * 使用小尾端来进行转换
     *
     * @return
     * @throws RuntimeException
     */
    @Override
    public byte[] toBytes() throws RuntimeException {
        ByteBuffer buffer = ByteBuffer.allocate(NSHEAD_LEN);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        try {
            buffer.putShort(header);
            buffer.putInt(serviceId);
            buffer.putLong(logId);
            buffer.putShort(bodyLen);
            buffer.putShort(resultCode);
        } catch (Exception e) {
            throw new RuntimeException("Nshead to byte[] failed", e);
        }

        return buffer.array();
    }
}
