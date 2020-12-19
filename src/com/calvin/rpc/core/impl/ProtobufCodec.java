package com.calvin.rpc.core.impl;

import com.calvin.rpc.core.Codec;
import com.calvin.rpc.util.Computable;
import com.calvin.rpc.util.ConcurrentCache;
import com.google.protobuf.GeneratedMessage;
import io.netty.handler.codec.CodecException;

import java.io.*;
import java.lang.reflect.Method;
import java.sql.DatabaseMetaData;
import java.util.concurrent.Callable;

public class ProtobufCodec implements Codec {


    @Override
    public Object decode(Class<?> clazz, byte[] bytes) throws CodecException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        return toObject(bytes);
    }

    /**
     * 数组转对象
     *
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            throw new CodecException("byteArray parse to object exception " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] encode(Class<?> clazz, Object object) throws CodecException {
        return toByteArray(object);
    }

    /**
     * 对象转数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)){
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CodecException("object parse to byteArray exception " + e.getMessage(), e);
        }
    }
}
