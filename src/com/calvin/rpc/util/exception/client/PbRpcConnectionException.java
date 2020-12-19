package com.calvin.rpc.util.exception.client;

public class PbRpcConnectionException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5278548816241146712L;

	/**
	 * Creates a new instance of PbRpcConnectionException.
	 */
	public PbRpcConnectionException() {
		super();
	}

	/**
	 * Creates a new instance of PbRpcConnectionException.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public PbRpcConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Creates a new instance of PbRpcConnectionException.
	 * 
	 * @param arg0
	 */
	public PbRpcConnectionException(String arg0) {
		super(arg0);
	}

	/**
	 * Creates a new instance of PbRpcConnectionException.
	 * 
	 * @param arg0
	 */
	public PbRpcConnectionException(Throwable arg0) {
		super(arg0);
	}

}
