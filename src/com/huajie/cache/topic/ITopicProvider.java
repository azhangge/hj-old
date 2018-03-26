package com.huajie.cache.topic;

import java.util.UUID;

import com.huajie.cache.CacheOperationMessage;

public interface ITopicProvider {

    public static final String _providerId = UUID.randomUUID().toString();
    
    

    /**
     * 向指定的topic发布消息
     *
     * @param msg
     */
    public void publish(CacheOperationMessage msg);

}
