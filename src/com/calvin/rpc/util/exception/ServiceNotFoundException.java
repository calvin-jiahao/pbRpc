package com.calvin.rpc.util.exception;

public class ServiceNotFoundException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5728672435089188056L;

    /**
     * Creates a new instance of ServiceNotFoundException.
     */
    public ServiceNotFoundException() {
        super();
    }

    /**
     * Creates a new instance of ServiceNotFoundException.
     *
     * @param message
     */
    public ServiceNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of ServiceNotFoundException.
     *
     * @param message
     * @param cause
     */
    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance of ServiceNotFoundException.
     *
     * @param cause
     */
    public ServiceNotFoundException(Throwable cause) {
        super(cause);
    }

}
