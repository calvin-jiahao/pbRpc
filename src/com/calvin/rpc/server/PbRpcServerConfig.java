package com.calvin.rpc.server;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 服务器配置
 */
public class PbRpcServerConfig {
    /**
     * keep alive
     *
     * @see ChannelOption#SO_KEEPALIVE
     */
    private boolean soKeepalive = true;

    /**
     * tcp nodelay
     *
     * @see ChannelOption#TCP_NODELAY
     */
    private boolean tcpNodelay = true;

    /**
     * so linger
     *
     * @see ChannelOption#SO_LINGER
     */
    private int soLinger = 2;

    /**
     * so backlog
     *
     * @see ChannelOption#SO_BACKLOG
     */
    private int soBacklog = 128;

    /**
     * receive buf size
     *
     * @see ChannelOption#SO_RCVBUF
     */
    private int soRcvbuf = 1024 * 64;

    /**
     * send buf size
     *
     * @see ChannelOption#SO_SNDBUF
     */
    private int soSndbuf = 1024 * 64;

    /**
     * so reuseaddr
     *
     * @see ChannelOption#SO_REUSEADDR
     */
    private boolean soReuseaddr = true;

    /**
     * low low water mark for write buffer
     *
     * @see ChannelOption#WRITE_BUFFER_WATER_MARK
     */
    private int lowWaterMark = 32 * 1024;

    /**
     * high high water mark for write buffer
     *
     * @see ChannelOption#WRITE_BUFFER_WATER_MARK
     */
    private int highWaterMark = 64 * 1024;

    /**
     * 由{@link IdleStateHandler#IdleStateHandler(int, int, int)}使用的构造参数，用于检查channel在此时间内没有读取操作，关闭channel以节省服务端资源
     */
    private int readerIdleTimeSeconds = 3600;

    /**
     * 由{@link IdleStateHandler#IdleStateHandler(int, int, int)}使用的构造参数，用于检查channel在此时间内没有写入操作，关闭channel以节省服务端资源
     */
    private int writerIdleTimeSeconds = 3600;

    /**
     * 由{@link IdleStateHandler#IdleStateHandler(int, int, int)}使用的构造参数，用于检查channel在此时间内没有写入而且没有读取操作，关闭channel以节省服务端资源
     */
    private int allIdleTimeSeconds = 3600;

    /**
     * 接受客户端请求的线程数
     */
    private int bossGroupCount = Runtime.getRuntime().availableProcessors();

    /**
     * 处理业务逻辑的线程
     */
    private int workerGroupCount = Runtime.getRuntime().availableProcessors() * 2;

    public boolean isSoKeepalive() {
        return soKeepalive;
    }

    public void setSoKeepalive(boolean soKeepalive) {
        this.soKeepalive = soKeepalive;
    }

    public boolean isTcpNodelay() {
        return tcpNodelay;
    }

    public void setTcpNodelay(boolean tcpNodelay) {
        this.tcpNodelay = tcpNodelay;
    }

    public int getSoLinger() {
        return soLinger;
    }

    public void setSoLinger(int soLinger) {
        this.soLinger = soLinger;
    }

    public int getSoBacklog() {
        return soBacklog;
    }

    public void setSoBacklog(int soBacklog) {
        this.soBacklog = soBacklog;
    }

    public int getSoRcvbuf() {
        return soRcvbuf;
    }

    public void setSoRcvbuf(int soRcvbuf) {
        this.soRcvbuf = soRcvbuf;
    }

    public int getSoSndbuf() {
        return soSndbuf;
    }

    public void setSoSndbuf(int soSndbuf) {
        this.soSndbuf = soSndbuf;
    }

    public boolean isSoReuseaddr() {
        return soReuseaddr;
    }

    public void setSoReuseaddr(boolean soReuseaddr) {
        this.soReuseaddr = soReuseaddr;
    }

    public int getLowWaterMark() {
        return lowWaterMark;
    }

    public void setLowWaterMark(int lowWaterMark) {
        this.lowWaterMark = lowWaterMark;
    }

    public int getHighWaterMark() {
        return highWaterMark;
    }

    public void setHighWaterMark(int highWaterMark) {
        this.highWaterMark = highWaterMark;
    }

    public int getReaderIdleTimeSeconds() {
        return readerIdleTimeSeconds;
    }

    public void setReaderIdleTimeSeconds(int readerIdleTimeSeconds) {
        this.readerIdleTimeSeconds = readerIdleTimeSeconds;
    }

    public int getWriterIdleTimeSeconds() {
        return writerIdleTimeSeconds;
    }

    public void setWriterIdleTimeSeconds(int writerIdleTimeSeconds) {
        this.writerIdleTimeSeconds = writerIdleTimeSeconds;
    }

    public int getAllIdleTimeSeconds() {
        return allIdleTimeSeconds;
    }

    public void setAllIdleTimeSeconds(int allIdleTimeSeconds) {
        this.allIdleTimeSeconds = allIdleTimeSeconds;
    }

    public int getBossGroupCount() {
        return bossGroupCount;
    }

    public void setBossGroupCount(int bossGroupCount) {
        this.bossGroupCount = bossGroupCount;
    }

    public int getWorkerGroupCount() {
        return workerGroupCount;
    }

    public void setWorkerGroupCount(int workerGroupCount) {
        this.workerGroupCount = workerGroupCount;
    }
}
