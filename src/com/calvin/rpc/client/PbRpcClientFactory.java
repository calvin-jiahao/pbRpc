package com.calvin.rpc.client;

import com.calvin.rpc.util.PbRpcConstants;

public class PbRpcClientFactory {

	/**
	 * 获取长连接池化的客户端
	 * 
	 * @param ip 远程服务ip
	 * @param port 远程服务端口
	 * @return linkCount 连接数量
	 */
	public static FixedChannelPbrpcClient buildPooledConnection(String ip, int port, int linkCount) {

		return new FixedChannelPbrpcClient(new PbRpcClientConfig(), ip, port, PbRpcConstants.DEFAULT_CLIENT_CONN_TIMEOUT, PbRpcConstants.DEFAULT_CLIENT_READ_TIMEOUT, linkCount);
	}

}
