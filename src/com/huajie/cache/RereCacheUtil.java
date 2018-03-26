/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huajie.cache.remote.RemoteCacheItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author huajie
 */
public class RereCacheUtil {

    public static RemoteCacheItem toRemote(RereCacheItem item) {
        RemoteCacheItem remote = new RemoteCacheItem();
        remote.setId(item.getId());
        remote.setGenTime(item.getGenTime());
        remote.setVisitTime(item.getVisitTime());
        GsonBuilder gb = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式      
                .setPrettyPrinting() //对json结果格式化.  
                .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

        Gson gson = gb.create();
        String json = gson.toJson(item.getObject());
        remote.setJson(json);
        return remote;
    }

    public static List<RemoteCacheItem> toRemoteList(List<RereCacheItem> items) {
        List<RemoteCacheItem> remotes = new ArrayList();
        for (RereCacheItem item : items) {
            remotes.add(toRemote(item));
        }
        return remotes;
    }
    
    public static List<RereCacheItem> fromRemoteList(List<RemoteCacheItem> items, Class cal) {
        List<RereCacheItem> remotes = new ArrayList();
        for (RemoteCacheItem item : items) {
            remotes.add(fromRemote(item,cal));
        }
        return remotes;
    }

    public static String toJson(Object obj) {
        GsonBuilder gb = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式      
                .setPrettyPrinting() //对json结果格式化.  
                .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  
        Gson gson = gb.create();
        String json = gson.toJson(obj);
        return json;
    }

    public static RereCacheItem fromRemote(RemoteCacheItem item, Class cal) {
        RereCacheItem local = new RereCacheItem();
        local.setId(item.getId());
        local.setGenTime(item.getGenTime());
        local.setVisitTime(item.getVisitTime());
        GsonBuilder gb = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性  
                .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式  
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")//时间转化为特定格式      
                .setPrettyPrinting() //对json结果格式化.  
                .setVersion(1.0);  //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.  
        //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么  
        //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.  

        Gson gson = gb.create();
        Object obj = gson.fromJson(item.getJson(), cal);
        local.setObject(obj);
        return local;
    }

}
