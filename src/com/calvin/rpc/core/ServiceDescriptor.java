package com.calvin.rpc.core;

import java.lang.reflect.Method;

public class ServiceDescriptor<T> {

    /**
     * 服务的id，唯一标示
     */
    private T serviceId;

    /**
     * 服务缓存的method
     */
    private Method method;

    /**
     * 服务具体的实现bean对象
     */
    private Object target;

    /**
     * 服务输入参数对象
     */
    private Class<?>[] argumentClass;

    /**
     * 服务返回参数对象
     */
    private Class<?> returnClass;

    public T getServiceId() {
        return serviceId;
    }

    public void setServiceId(T serviceId) {
        this.serviceId = serviceId;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Class<?>[] getArgumentClass() {
        return argumentClass;
    }

    public void setArgumentClass(Class<?>[] argumentClass) {
        this.argumentClass = argumentClass;
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(Class<?> returnClass) {
        this.returnClass = returnClass;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceId=");
        sb.append(serviceId);
        sb.append(", ");
        sb.append(returnClass.getSimpleName());
        sb.append(" ");
        sb.append(target.getClass().getSimpleName());
        sb.append(".");
        sb.append(method.getName());
        sb.append("(");
        sb.append(argumentClass.getClass().getSimpleName());
        sb.append(")");
        return sb.toString();
    }
}
