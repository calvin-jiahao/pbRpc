package com.calvin.rpc.core;

import com.calvin.rpc.util.exception.PreCondition;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistry<T> {

    /**
     * 服务注册的字典 <br/>
     * 键为服务标示<code>KEY</code>，值为服务具体描述
     *
     * @see ServiceDescriptor
     */
    private Map<T, ServiceDescriptor<T>> serviceDescriptors;

    /**
     * 注册单例引用
     */
    private static ServiceRegistry instance;

    /**
     * 获取服务注册对象
     *
     * @return
     */
    public static <E> ServiceRegistry<E> getInstance() {
        if (instance == null) {
            synchronized (ServiceRegistry.class) {
                if (instance == null) {
                    instance = new ServiceRegistry<E>();
                }
            }
        }
        return instance;
    }

    /**
     * Creates a new instance of ServiceRegistry.
     */
    public ServiceRegistry() {
        serviceDescriptors = new HashMap<T, ServiceDescriptor<T>>();
    }

    /**
     * 根据标示<code>serviceId</code>获取服务描述
     *
     * @param serviceId 服务的唯一标示
     * @return 服务描述
     * @throws IllegalStateException
     */
    public ServiceDescriptor<T> getServiceDescriptorByKey(T serviceId) throws IllegalStateException {
        PreCondition.checkNotNull(serviceId, "ServiceId cannot be null");
        PreCondition.checkState(instance != null, "ServiceRegistry not init yet");
        return serviceDescriptors.get(serviceId);
    }

    /**
     * 加入服务描述
     *
     * @param serviceId         服务的唯一标示
     * @param serviceDescriptor 服务描述
     */
    public void addServiceDescriptor(T serviceId, ServiceDescriptor<T> serviceDescriptor) {
        PreCondition.checkNotNull(serviceId, "ServiceId cannot be null");
        PreCondition.checkState(instance != null, "ServiceRegistry not init yet");
        if (serviceDescriptors.containsKey(serviceId)) {
            PreCondition.ServiceIdRepetition("serviceId=" + serviceId + " will be override with " + serviceDescriptor);
        }
        this.serviceDescriptors.put(serviceId, serviceDescriptor);
    }
}
