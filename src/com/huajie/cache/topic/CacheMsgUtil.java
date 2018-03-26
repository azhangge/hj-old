package com.huajie.cache.topic;

import java.util.List;

import com.huajie.cache.CacheOperationMessage;
import com.huajie.cache.CacheOperations;
import com.huajie.util.Cat;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie
 */
public class CacheMsgUtil {

    /**
     * 发布同步消息
     * @param msg 
     */
    public static void publish(CacheOperationMessage msg) {
        //获取JMS发送者并发送消息
        //Destination topic = SpringHelper.getSpringBean("topicDestination");
        try {
            ITopicProvider sender = SpringHelper.getSpringBean("TopicProvider");
            sender.publish(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 发布同步更新消息
     * @param insName
     * @param key
     * @param value 
     */
    public static void publishAdd(String insName,String key,Object value) {
        try {
            CacheOperationMessage msg = new CacheOperationMessage();
            msg.setProviderId(Cat.getSysId());
            msg.setKey(key);
            msg.setValue(value);
            msg.setInstanceName(insName);
            msg.setOperation(CacheOperations.ADD);
            CacheMsgUtil.publish(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发布同步更新消息
     * @param insName
     * @param key
     * @param value 
     */
    public static void publishUpdate(String insName,String key,Object value) {
        try {
            CacheOperationMessage msg = new CacheOperationMessage();
            msg.setProviderId(Cat.getSysId());
            msg.setKey(key);
            msg.setValue(value);
            msg.setInstanceName(insName);
            msg.setOperation(CacheOperations.UPDATE);
            CacheMsgUtil.publish(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 发布同步删除消息
     * @param insName
     * @param key
     */
    public static void publishDelete(String insName,String key) {
        try {
            CacheOperationMessage msg = new CacheOperationMessage();
            msg.setProviderId(Cat.getSysId());
            msg.setKey(key);
            msg.setInstanceName(insName);
            msg.setOperation(CacheOperations.DELETE);
            CacheMsgUtil.publish(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 发布同步批量删除消息
     * @param insName
     * @param keys
     */
    public static void publishBatchDelete(String insName,List<String> keys) {
        try {
            CacheOperationMessage msg = new CacheOperationMessage();
            msg.setProviderId(Cat.getSysId());
            msg.setKeys(keys);
            msg.setInstanceName(insName);
            msg.setOperation(CacheOperations.BATCH_DELETE);
            CacheMsgUtil.publish(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 发布同步销毁实例消息
     * @param insName
     */
    public static void publishDestroy(String insName) {
        try {
            CacheOperationMessage msg = new CacheOperationMessage();
            
            msg.setProviderId(Cat.getSysId());
            msg.setInstanceName(insName);
            msg.setOperation(CacheOperations.DESTROY);
            CacheMsgUtil.publish(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
