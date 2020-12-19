package com.calvin.rpc.util.exception.client;

/**
 * <pre>
 * 服务端执行异常，当通过反射调用本地方法时抛出异常或者发生其他未知异常抛出
 * </pre>
 */
public class ServerExecutionException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9197528117572815778L;

	/**
	 * Creates a new instance of ServerExecutionException.
	 */
	public ServerExecutionException() {
		super();
	}

	/**
	 * Creates a new instance of ServerExecutionException.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public ServerExecutionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Creates a new instance of ServerExecutionException.
	 * 
	 * @param arg0
	 */
	public ServerExecutionException(String arg0) {
		super(arg0);
	}

	/**
	 * Creates a new instance of ServerExecutionException.
	 * 
	 * @param arg0
	 */
	public ServerExecutionException(Throwable arg0) {
		super(arg0);
	}

}
