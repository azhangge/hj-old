package com.huajie.cache.index;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 缓存索引实例中的条目，记录了关键字及其对应的数据ID集
 * @author huajie.com
 */
public class RereCacheIndexItem {

    String id = UUID.randomUUID().toString();
    Object keyWord;//索引条目的关键字
    Set<String> ids = new ConcurrentSkipListSet<String>();//保存了关键字key对应的所有元素id

    /**
     * 更新该索引关键词对应的数据成员ID
     *
     * @param id
     */
    public void putMember(String id) {
        this.ids.add(id);
    }

    /**
     * 删除索引关键词对应的成员ID
     * @param id 
     */
    public void deleteMember(String id) {
        this.ids.remove(id);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(Object keyWord) {
        this.keyWord = keyWord;
    }


    public Set<String> getIds() {
        return ids;
    }

    public void setIds(Set<String> ids) {
        this.ids = ids;
    }

}
