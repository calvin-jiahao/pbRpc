package com.calvin.rpc.appinterface;

/**
 * 客户端及服务器各实现该接口
 */
public interface ILoginServerRpc {

    public boolean isValid();

    public boolean reloadTemplate(String tables);

}
