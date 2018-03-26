package com.huajie.cache;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 缓存条目，装载在缓存实例中
 * @author Administrator
 */
public class RereCacheItem<T>  implements Serializable{
    
    private String id = UUID.randomUUID().toString();
    private T object;//缓存的对象
    private Date genTime=new Date();//创建时间
    private Date visitTime=new Date();//上次访问时间
    private long lifeLen = 30;//生命时长，以秒为单位，若设为0，表示永生。

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
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

	public long getLifeLen() {
		return lifeLen;
	}

	public void setLifeLen(long lifeLen) {
		this.lifeLen = lifeLen;
	}
 
}
