package com.calvin.rpc.util.exception.client;

import com.calvin.rpc.util.exception.ErrorCode;
import com.calvin.rpc.util.exception.ServiceNotFoundException;
import io.netty.handler.codec.CodecException;

public class ExceptionUtil {

    /**
     * 根据{@see NsHeader}中保存的<tt>flags</tt>中定义的errorCode构造异常
     *
     * @param errorCode
     * @return
     */
    public static RuntimeException buildFromErrorCode(ErrorCode errorCode) {
        if (errorCode == ErrorCode.PROTOBUF_CODEC_ERROR) {
            return new CodecException("Serialization failed at server, please check proto compatiblity");
        }
        if (errorCode == ErrorCode.SERVICE_NOT_FOUND) {
            return new ServiceNotFoundException("Service not found, please check serviceId specified");
        }
        if (errorCode == ErrorCode.INVOCATION_TARGET_EXCEPTION || errorCode == ErrorCode.UNEXPECTED_ERROR) {
            return new ServerExecutionException("Exception occurred at server, and cause can be only get from server");
        }
        if (errorCode == ErrorCode.COMMUNICATION_ERROR) {
            return new CommunicationException();
        }

        return new RuntimeException("Failed to specify server error though there is error happened");
    }

}
