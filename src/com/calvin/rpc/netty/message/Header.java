package com.calvin.rpc.netty.message;

/**
 * 通用消息头，用于RPC通讯消息属于header+body方式的交互
 *
 */
public interface Header {

    /**
     * 头固定字节长度
     *
     * @return
     */
    int getFixedHeaderLen();

    /**
     * 消息body体长度
     *
     * @param bodyLen
     */
    void setBodyLen(short bodyLen);

    /**B
     * 获取消息body体长度
     *
     * @return
     */
    short getBodyLen();

    /**
     * 从字节码构造头
     *
     * @param input
     */
    void wrap(byte[] input);

    /**
     * 头序列化为字节码
     *
     * @return
     * @throws RuntimeException
     */
    byte[] toBytes() throws RuntimeException;

}
