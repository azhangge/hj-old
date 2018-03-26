package com.hjedu.platform.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheItem;
import com.huajie.cache.RereCacheManager;
import com.huajie.util.JsfHelper;

@ManagedBean
public class CacheList {

    IRereCacheInstance instance;
    List<RereCacheItem> items;

    public List<RereCacheItem> getItems() {
        return items;
    }

    public void setItems(List<RereCacheItem> items) {
        this.items = items;
    }

    public IRereCacheInstance getInstance() {
        return instance;
    }

    public void setInstance(IRereCacheInstance instance) {
        this.instance = instance;
    }

    
    
    
    @PostConstruct
    public void init() {
        String id = JsfHelper.getRequest().getParameter("id");
        if (id != null) {
            instance=RereCacheManager.getLocalInstance(id);
            Collection<RereCacheItem> is=instance.getItems().values();
            items=new ArrayList();
            items.addAll(is);
            
        }

    }

}
