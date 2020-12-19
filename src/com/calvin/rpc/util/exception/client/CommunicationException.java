package com.calvin.rpc.util.exception.client;

/**
 * <pre>
 * 服务端和客户端通信交互中发生的异常
 * </pre>
 */
public class CommunicationException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4702483986620269902L;

	/**
	 * Creates a new instance of CommunicationException.
	 */
	public CommunicationException() {
		super();
	}

	/**
	 * Creates a new instance of CommunicationException.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public CommunicationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Creates a new instance of CommunicationException.
	 * 
	 * @param arg0
	 */
	public CommunicationException(String arg0) {
		super(arg0);
	}

	/**
	 * Creates a new instance of CommunicationException.
	 * 
	 * @param arg0
	 */
	public CommunicationException(Throwable arg0) {
		super(arg0);
	}

}
