package com.calvin.rpc.core;

public interface ServiceLocator<T> {

    /**
     * 根据服务标识获取服务描述
     *
     * @param key
     * @return
     */
    ServiceDescriptor<T> getServiceDescriptor(T key);

    /**
     * 注入服务
     *
     * @param serviceBean
     * @return
     */
    boolean registerService(Object serviceBean);

}
