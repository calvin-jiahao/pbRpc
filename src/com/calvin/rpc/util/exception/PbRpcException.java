package com.calvin.rpc.util.exception;

/**
 * <pre>
 * 客户端通用的关于PbRpc的异常
 * </pre>
 */
public class PbRpcException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1278766007974170994L;

    /**
     * Creates a new instance of PbrpcException.
     */
    public PbRpcException() {
        super();
    }

    /**
     * Creates a new instance of PbrpcException.
     *
     * @param message
     */
    public PbRpcException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of PbrpcException.
     *
     * @param cause
     */
    public PbRpcException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of PbrpcException.
     *
     * @param message
     * @param cause
     */
    public PbRpcException(String message, Throwable cause) {
        super(message, cause);
    }

}
