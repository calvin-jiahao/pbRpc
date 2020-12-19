package com.calvin.rpc.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    /**
     * 获取某个类的所有实例方法
     *
     * @param clazz
     * @return
     */
    public static Method[] getAllInstanceMethods(final Class<?> clazz) {
        if (clazz == null) {
            return  null;
        }

        List<Method> methods = new ArrayList<>();
        for (Class<?> itr = clazz; hasSuperClass(itr);) {
            for (Method method : itr.getDeclaredMethods()) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    methods.add(method);
                }
            }
            itr = itr.getSuperclass();
        }
        Method[] arr = methods.toArray(new Method[methods.size()]);
        return arr;
    }

    /**
     * 判断某个类是否含有父类或者接口
     *
     * @param clazz
     * @return
     */
    public static boolean hasSuperClass(Class<?> clazz) {
        return (clazz != null) && !clazz.equals(Object.class);
    }

    /**
     * 判断某个类是否是void类型
     *
     * @param clazz
     * @return
     */
    public static boolean isVoid(Class<?> clazz) {
        return clazz == void.class;
    }

}
