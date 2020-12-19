package com.calvin.rpc.core.impl;

import com.calvin.rpc.client.PbRpcClient;
import com.calvin.rpc.client.callback.CallFuture;
import com.calvin.rpc.core.Asyn;
import com.calvin.rpc.netty.message.PbRpcMsg;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ServerInvokeHandler implements InvocationHandler {

    private PbRpcClient client;

    public ServerInvokeHandler(PbRpcClient client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        int serviceId = client.getServerId(method);
        PbRpcMsg msg = new PbRpcMsg();
        msg.setServiceId(serviceId);
        if (args != null) {
            msg.setData(ProtobufCodec.toByteArray(args));
        }

        CallFuture<?> future = client.asyncTransport(returnType, msg);

        if (method.getAnnotation(Asyn.class) != null) {
            return future;
        }

        return future.get();
    }
}
