package com.hjedu.platform.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheManager;

/**
 *
 * @author huajie
 */
@ManagedBean
public class CacheInstanceList {

    List<IRereCacheInstance> instances;

    public List<IRereCacheInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<IRereCacheInstance> instances) {
        this.instances = instances;
    }

    @PostConstruct
    public void init() {
        instances = new ArrayList();
        Collection<IRereCacheInstance> ts = RereCacheManager.getContainer().getInstancesMap().values();
        for (IRereCacheInstance t : ts) {
            instances.add(t);
        }
    }

}
