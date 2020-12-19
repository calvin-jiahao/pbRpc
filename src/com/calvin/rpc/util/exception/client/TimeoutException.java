package com.calvin.rpc.util.exception.client;

public class TimeoutException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1503433640157346327L;

	/**
	 * Creates a new instance of TimeoutException.
	 */
	public TimeoutException() {
		super();
	}

	/**
	 * Creates a new instance of TimeoutException.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public TimeoutException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Creates a new instance of TimeoutException.
	 * 
	 * @param arg0
	 */
	public TimeoutException(String arg0) {
		super(arg0);
	}

	/**
	 * Creates a new instance of TimeoutException.
	 * 
	 * @param arg0
	 */
	public TimeoutException(Throwable arg0) {
		super(arg0);
	}

}