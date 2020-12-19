package com.calvin.rpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * <pre>
 * 字节码工具
 * </pre>
 */
public class ByteUtil {

    private static final int BYTE_SIZE = 1024 * 32;

    private static final Logger LOG = LoggerFactory.getLogger(ByteUtil.class);

    /**
     * 默认字符编码
     */
    private static final String STR_ENCODE = "UTF-8";

    /**
     * 将字符串转换成UTF-8的字节码
     *
     * @param input
     * @return byte[]
     */
    public static byte[] convertStringToBytes(String input) {
        byte[] bytes = null;
        if (input != null) {
            try {
                bytes = input.getBytes(STR_ENCODE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    /**
     * 将字符串转换成UTF-8的字节码，阶段16个字节的长度
     *
     * @param input
     * @param length
     * @return
     */
    public static byte[] convertStringToBytes(String input, int length) {
        byte[] temp = convertStringToBytes(input);
        byte[] result = new byte[length];
        System.arraycopy(temp, 0, result, 0, length);
        return result;
    }

    /**
     * 字节码转字符串
     *
     * @param bytes
     * @return
     */
    public static String convertBytesToString(byte[] bytes) {
        String str = null;
        if (bytes != null) {
            try {
                str = new String(bytes, STR_ENCODE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    /**
     * 获取非空字节码
     *
     * @param data
     * @return
     */
    public static byte[] getNonNUllBytes(byte[] data) {
        if (data == null) {
            return new byte[0];
        }
        return data;
    }

    /**
     * read inputstream to a byte array
     *
     * @param body
     * @param in
     * @return
     * @throws IOException
     */
    public static int read(byte[] body, InputStream in) throws IOException{
        byte[] buffer = new byte[BYTE_SIZE];
        int readLength = 0;
        int offset = 0;
        int bytesRead = -1;

        try {
            while (offset < body.length) {
                if (body.length - offset > buffer.length) {
                    readLength = buffer.length;
                } else {
                    readLength = body.length - offset;
                }
                bytesRead = in.read(buffer, 0, readLength);
                if (bytesRead != -1) {
                    System.arraycopy(buffer, 0, body, offset, bytesRead);
                    offset += bytesRead;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            LOG.warn("bytesRead:" + bytesRead + "; offset:" + offset + "; BODT_length:" + body.length, e);
            throw new IOException(e.getMessage());
        } finally {
            readLength = offset;
        }
        return readLength;
    }

}
