package com.huajie.cache.topic;

import java.util.UUID;

import com.huajie.cache.CacheOperationMessage;

public class TopicProvider implements ITopicProvider{

    public static String providerId = UUID.randomUUID().toString();
    

    /**
     * 初始化Hazelcast，从配置文件创建一个Hazelcast client.
     */
    public TopicProvider() {
        
    }

    /**
     * 向指定的topic发布消息
     *
     * @param msg
     */
    public void publish(CacheOperationMessage msg) {
        
    }

}
