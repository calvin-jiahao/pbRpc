package com.calvin.rpc.util.exception;

public class ServiceIdRepetitionException extends RuntimeException {

    private static final long serialVersionUID = 8942836182913812943L;

    /**
     * Constructs a {@code ServiceIdRepetitionException} with no detail message.
     */
    public ServiceIdRepetitionException() {
        super();
    }

    /**
     * Constructs a {@code ServiceIdRepetitionException} with the specified detail message.
     *
     * @param s the detail message.
     */
    public ServiceIdRepetitionException(String s) {
        super(s);
    }
}
