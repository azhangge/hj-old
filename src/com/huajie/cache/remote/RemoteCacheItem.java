package com.huajie.cache.remote;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 缓存条目，用于装载在远程缓存服务器
 * @author Administrator
 */
public class RemoteCacheItem  implements Serializable{
    
    private String id = UUID.randomUUID().toString();
    private String json;//缓存的对象
    private Date genTime=new Date();//创建时间
    private Date visitTime=new Date();//上次访问时间
    //private long lifeLen = 600;//生命时长，以秒为单位，若设为0，表示永生。

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    
}
