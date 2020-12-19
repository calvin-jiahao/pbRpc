package com.calvin.rpc.core.impl;

import com.calvin.rpc.core.ServiceDescriptor;
import com.calvin.rpc.core.ServiceLocator;
import com.calvin.rpc.core.ServiceRegistry;
import com.calvin.rpc.util.ReflectionUtil;
import com.calvin.rpc.util.exception.PreCondition;
import com.google.protobuf.GeneratedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

public class IdKeyServiceLocator implements ServiceLocator<Integer> {

    private static final Logger Log = LoggerFactory.getLogger(IdKeyServiceLocator.class);

    /**
     * 服务描述的缓存，内部可以按照服务标示查找
     */
    private ServiceRegistry<Integer> serviceRegistry;

    /**
     * Creates a new instance of IdKeyServiceLocator.
     */
    public IdKeyServiceLocator() {
        serviceRegistry = ServiceRegistry.getInstance();
    }


    @Override
    public ServiceDescriptor<Integer> getServiceDescriptor(Integer serviceId) {
        PreCondition.checkNotNull(serviceId, "ServiceId cannot be null");
        return serviceRegistry.getServiceDescriptorByKey(serviceId);
    }

    /**
     * 注册serviceBean，暴露接口
     *
     * @param serviceBean
     * @return
     */
    @Override
    public boolean registerService(Object serviceBean) {
        Method[] methods = serviceBean.getClass().getInterfaces()[0].getMethods();
        for (Method ms : methods) {
            //serviceId 改为方法hashcode
            int serviceId = ms.toString().hashCode();
            ServiceDescriptor<Integer> desc = new ServiceDescriptor<Integer>();
            desc.setServiceId(serviceId);
            desc.setMethod(ms);
            desc.setTarget(serviceBean);
            desc.setArgumentClass(ms.getParameterTypes());
            desc.setReturnClass(ms.getReturnType());
            serviceRegistry.addServiceDescriptor(serviceId, desc);
            Log.info(String.format("Register service serviceId=[%d], %s %s#%s(%s) successfully", serviceId, ms.getReturnType().getSimpleName(), serviceBean.getClass().getName(), ms.getName(),
                    Arrays.toString(ms.getParameterTypes())));
        }
        return true;
    }

}
