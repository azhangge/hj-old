package com.huajie.cache.index;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存索引实例，一个缓存实例通常有多个索引实例
 *
 * @author huajie.com
 */
public class RereCacheIndexInstance {

    String id = UUID.randomUUID().toString();
    String name;//缓存索引实例名字，通常是模型属性名称
    Map<String, RereCacheIndexItem> indexItems = new ConcurrentHashMap<String, RereCacheIndexItem>();//索引条目/元素

    /**
     *
     * @param value 准备要索引的关键字值
     * @param id 数据id
     */
    public void putData(String value, String id) {
        RereCacheIndexItem item = this.indexItems.get(value);
        if (item == null) {
            item = new RereCacheIndexItem();
            item.setKeyWord(value);
            this.indexItems.put(value, item);
        }
        item.putMember(id);
    }

    /**
     * 对某个特定的关键词中删除数据id
     *
     * @param value
     * @param id
     */
    public void deleteData(String value, String id) {
        RereCacheIndexItem item = this.indexItems.get(value);
        if (item != null) {
            Set<String> set = item.getIds();
            set.remove(id);
        }
    }

    /**
     * 从整个索引实例中删除数据id
     *
     * @param id
     */
    public void deleteData(String id) {
        Set<String> cs = this.indexItems.keySet();
        for (String keyWord : cs) {
            this.deleteData(keyWord, id);
        }
    }

    /**
     * 从索引实例中删除某关键字
     * @param word 
     */
    public void deleteKeyWord(String word) {
        this.indexItems.remove(word);
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, RereCacheIndexItem> getIndexItems() {
        return indexItems;
    }

    public void setIndexItems(Map<String, RereCacheIndexItem> indexItems) {
        this.indexItems = indexItems;
    }

}
