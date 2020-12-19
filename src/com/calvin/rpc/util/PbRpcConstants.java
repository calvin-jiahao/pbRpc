package com.calvin.rpc.util;

/**
 * <pre>
 * 一些默认的常量
 * </pre>
 */
public class PbRpcConstants {

	/**
	 * 默认客户端连接超时时间，单位毫秒
	 */
	public static final int DEFAULT_CLIENT_CONN_TIMEOUT = 5000;

	/**
	 * 默认客户端调用读超时时间，单位毫秒
	 */
	public static final int DEFAULT_CLIENT_READ_TIMEOUT = 60000;

	/**
	 * 默认客户端超时调用检测器启动时间，单位毫秒
	 */
	public static final int CLIENT_TIMEOUT_EVICTOR_DELAY_START_TIME = 5000;

	/**
	 * 默认客户端超时调用检测器检测间隔，单位毫秒
	 */
	public static final int CLIENT_TIMEOUT_EVICTOR_CHECK_INTERVAL = 5000;

	/**
	 * 默认客户端断线重连检测器启动时间，单位毫秒
	 */
	public static final int CLIENT_RECONNECT_TASK_DELAY_START_TIME = 5000;

	/**
	 * 默认客户端断线重连检测器检测间隔，单位毫秒
	 */
	public static final int CLIENT_RECONNECT_TASK_CHECK_INTERVAL = 5000;

}
