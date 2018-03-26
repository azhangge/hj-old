package com.huajie.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.huajie.cache.remote.RemoteCacheItem;

/**
 * 本类的缓存通信中返回的消息
 * @author huajie.com
 */
public class CacheResponseMessage implements Serializable {

    private String id = UUID.randomUUID().toString();
    private RemoteCacheItem item;//返回的单个条目
    private List<RemoteCacheItem> items;//返回的条目列表
    private Date genTime = new Date();//生成时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RemoteCacheItem> getItems() {
        return items;
    }

    public void setItems(List<RemoteCacheItem> items) {
        this.items = items;
    }

    

    public RemoteCacheItem getItem() {
        return item;
    }

    public void setItem(RemoteCacheItem item) {
        this.item = item;
    }

    

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

}
