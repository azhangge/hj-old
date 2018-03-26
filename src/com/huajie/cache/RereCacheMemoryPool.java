package com.huajie.cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.huajie.cache.serialize.ICacheSerial;

/**
 * 缓存容器，装载了缓存实例，为一线程程序，能够自检缓存实例中的条目是否已过期 此缓存实现了内存缓存池，暂不具备持久化功能及分布式运行能力
 *
 * @author huajie.com
 */
public class RereCacheMemoryPool implements Runnable, Serializable {
    
    private Map<String, IRereCacheInstance> instancesMap = new HashMap();
    public boolean ifOpen = true;
    
    public Map<String, IRereCacheInstance> getInstancesMap() {
        return instancesMap;
    }
    
    public void setInstancesMap(Map<String, IRereCacheInstance> instancesMap) {
        this.instancesMap = instancesMap;
    }
    
    @Override
    public void run() {
        this.loadConfig();
        while (true) {
            this.checkItemsLives();
            try {
                Thread.sleep(30000);//每30秒检查一次
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!ifOpen) {
                break;
            }
        }
    }

    /**
     * 加载配置信息
     */
    private void loadConfig() {
    }

    /**
     * 此方法用于检查缓存实例中条目的生命周期，过期的可以删除
     */
    private void checkItemsLives() {
        long time = System.currentTimeMillis();
        Collection<IRereCacheInstance> instances = this.instancesMap.values();
        for (IRereCacheInstance ci : instances) {
            ci.checkLives();
            ICacheSerial cs = ci.getCacheSerial();
            if (cs != null) {
                cs.checkLives(ci.getLifeLen());
            }
        }
    }
    
}
