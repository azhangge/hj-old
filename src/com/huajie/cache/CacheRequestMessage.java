package com.huajie.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.huajie.cache.remote.RemoteCacheItem;

/**
 *
 * @author huajie
 */
public class CacheRequestMessage implements Serializable {

    private String id = UUID.randomUUID().toString();
    private String providerId;//消息发送者ID
    private String instanceName;//操作的缓存实例名称
    private RemoteCacheItem item;
    private List<RemoteCacheItem> items;
    private CacheOperations operation;//操作类型，有效值为：update,add,delete,find,findall,findmany
    private String queryIndex;//查询的索引名称
    private String keyWord;//查询的关键词
    int startIndex=0;//findmany情况下的起始index
    int num=10;//findmany情况下的获取数量
    private Date genTime = new Date();//生成时间

    

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

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


    public CacheOperations getOperation() {
        return operation;
    }

    public void setOperation(CacheOperations operation) {
        this.operation = operation;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    
    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public RemoteCacheItem getItem() {
        return item;
    }

    public void setItem(RemoteCacheItem item) {
        this.item = item;
    }

    public String getQueryIndex() {
        return queryIndex;
    }

    public void setQueryIndex(String queryIndex) {
        this.queryIndex = queryIndex;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    
    
}
