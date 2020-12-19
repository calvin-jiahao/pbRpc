package com.calvin.rpc.netty.message;

import com.calvin.rpc.util.exception.ErrorCode;

public class PbRpcMsg {

    /**
     * 服务的标识id，一般客户端需要制定该id，服务端可以利用这个id路由到某个方法上调用。<br/>
     * 实际就是{@link NsHeader}头中<tt>methodId</tt>，用于在服务端和客户端传递
     */
    private int serviceId;

    /**
     * 服务上下文logId，用于tracing使用。 <br/>
     * 实际就是{@link NsHeader}头中<tt>logId</tt>，用于在服务端和客户端传递
     */
    private long logId;

    /**
     * 一些不关业务逻辑处理的errorCode，由框架负责处理标示，发送请求时候请无设置该值
     */
    private ErrorCode errorCode;

    /**
     * 传输的经过protobuf序列化的字节码
     */
    private byte[] data;

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

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public PbRpcMsg setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PbRpcMsg[logId=");
        sb.append(logId);
        sb.append(", serviceId=");
        sb.append(serviceId);
        sb.append(", dataLength=");
        sb.append((data == null) ? 0 : data.length);
        if (errorCode != null) {
            sb.append(", errorCode=");
            sb.append(errorCode);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 根据NsHead构造
     *
     * @param header
     * @return
     */
    public static PbRpcMsg of(NsHeader header) {
        PbRpcMsg msg = new PbRpcMsg();
        msg.setServiceId(header.getServiceId());
        msg.setLogId(header.getLogId());
        msg.setErrorCode(ErrorCode.get(header.getResultCode()));
        return msg;
    }

    /**
     * 简单信息的拷贝复制，不拷贝字节码
     *
     * @param msg
     * @return
     */
    public static PbRpcMsg copyLiteOf(PbRpcMsg msg) {
        PbRpcMsg newMsg = new PbRpcMsg();
        newMsg.setServiceId(msg.getServiceId());
        newMsg.setLogId(msg.getLogId());
        newMsg.setErrorCode(msg.getErrorCode());
        return newMsg;
    }
}
