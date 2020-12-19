package com.calvin.rpc.util;

/**
 * <pre>
 * 有符号 - 无符号转换工具
 * </pre>
 */
public class UnsignedSwitch {

    /**
     * 无符号int到long
     *
     * @param x
     * @return
     */
    public static long uintToLong(int x) {
        return x & 0xffffffffL;
    }

    /**
     * 无符号short到int
     *
     * @param x
     * @return
     */
    public static int uShortToInt(short x) {
        return (int) (x & 0xffff);
    }

    /**
     * long到无符号int
     *
     * @param x
     * @return
     */
    public static int longToUint(long x) {
        return (int) (x & 0xffffffff);
    }

    /**
     * int到无符号short
     *
     * @param x
     * @return
     */
    public static short intToUshort(int x) {
        return (short) (x & 0xffff);
    }

}
