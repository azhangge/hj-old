package com.huajie.cache.topic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huajie.cache.CacheOperationMessage;
import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheInstanceReplicated;
import com.huajie.cache.RereCacheManager;
import com.huajie.util.Cat;

import java.util.List;

/**
 *
 * @author huajie
 */
public class MessageHandler {

    public static void handle(CacheOperationMessage msg) {
        try {
            System.out.println(msg.getInstanceName()+":"+Cat.getSysId()+":"+msg.getProviderId()+":"+msg.getOperation());
            if (!Cat.getSysId().equals(msg.getProviderId())) {
                String instanceName = msg.getInstanceName();
                IRereCacheInstance ci = RereCacheManager.getReplicatedInstance(instanceName);
                //只有冗余缓存才同步
                if (ci instanceof RereCacheInstanceReplicated) {
                    RereCacheInstanceReplicated ci2 = (RereCacheInstanceReplicated) ci;
                    if (null != msg.getOperation()) {
                        switch (msg.getOperation()) {
                            case ADD:
                                ci2.addWithoutMsg(msg.getKey(), msg.getValue());
                                break;
                            case UPDATE:
                                ci2.updateWithoutMsg(msg.getKey(), msg.getValue());
                                break;
                            case DELETE:
                                ci2.removeWithoutMsg(msg.getKey());
                                break;
                            case BATCH_DELETE:
                                List<String> keys = msg.getKeys();
                                if (keys != null) {
                                    for (String key : keys) {
                                        ci2.removeWithoutMsg(key);
                                    }
                                }
                                break;
                            case CLEAR:
                                ci2.removeAllWithoutMsg();
                                break;
                            case DESTROY:
                                ci2.destroyWithoutMsg();
                                break;
                            default:
                                break;
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handle(String msg) {
        try {
            GsonBuilder gb = new GsonBuilder();
            Gson gson = gb.create();
            CacheOperationMessage msg2 = gson.fromJson(msg, CacheOperationMessage.class);
            handle(msg2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
