package com.calvin.rpc.util.exception;

public enum ErrorCode {
    SERVICE_NOT_FOUND((short) 0x7f, "Service not found"),
    PROTOBUF_CODEC_ERROR((short) 0x7e, "Protobuf codec failed "),
    INVOCATION_TARGET_EXCEPTION((short) 0x7d, "Invocation method on target bean failed "),
    UNEXPECTED_ERROR((short) 0x7c, "Unexpected error occurred which should not happen "),
    COMMUNICATION_ERROR((short) 0x7b, "Communication error occurred ");

    /**
     * 错误码
     */
    private short value = 0;

    /**
     * 错误消息
     */
    private String message = "";

    /**
     * Creates a new instance of ErrorCode.
     *
     * @param value
     * @param message
     */
    private ErrorCode(short value, String message) {
        this.value = value;
        this.message = message;
    }

    public short getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据错误码返回错误枚举
     *
     * @param errorCode
     * @return
     */
    public static ErrorCode get(short errorCode) {
        if (SERVICE_NOT_FOUND.getValue() == errorCode) {
            return SERVICE_NOT_FOUND;
        }
        if (PROTOBUF_CODEC_ERROR.getValue() == errorCode) {
            return PROTOBUF_CODEC_ERROR;
        }
        if (INVOCATION_TARGET_EXCEPTION.getValue() == errorCode) {
            return INVOCATION_TARGET_EXCEPTION;
        }
        if (UNEXPECTED_ERROR.getValue() == errorCode) {
            return UNEXPECTED_ERROR;
        }
        if (COMMUNICATION_ERROR.getValue() == errorCode) {
            return COMMUNICATION_ERROR;
        }
        return null;
    }
}
