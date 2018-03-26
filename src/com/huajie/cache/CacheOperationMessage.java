
package com.huajie.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author huajie
 */
public class CacheOperationMessage implements Serializable{
    private String id=UUID.randomUUID().toString();
    private String providerId;//消息发送者ID
    private String instanceName;//操作的缓存实例名称
    private String key;//数据的key
    private Object value;//数据值
    private List<String> keys=new ArrayList();//用于批量操作时保存的ID列表，如批量删除
    private List<Object> values=new ArrayList();//用户批量操作时保存的对象组
    private CacheOperations  operation;//操作类型，有效值为：update,add,delete
    private Date genTime=new Date();//生成时间
    
    //合法的操作类型
    public enum OperationsEnum {
        update,add,delete;
    }

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public CacheOperations getOperation() {
        return operation;
    }

    public void setOperation(CacheOperations operation) {
        this.operation = operation;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    
    
}
