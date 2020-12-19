package com.calvin.rpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReConnectTask extends AbstractBaseTask {

    private static final Logger Log = LoggerFactory.getLogger(ReConnectTask.class);

    PbRpcClient client = null;

    public ReConnectTask(PbRpcClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            boolean result = client.reConnect();
            Log.info("try connect to " + client.toString() + (result ? " succeed" : " failed") + ".");
            if (result) {
                cancel();
                Log.info("Try connect to " + client.toString() + " succeed,tryRunner exit.");
            }

        } catch (Exception e) {
            Log.error("TryRunner has exception:", e);
        }
    }

}
