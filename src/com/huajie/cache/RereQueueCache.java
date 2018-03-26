package com.huajie.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 本类用Ehcache实现了缓存队列接口，RereCache是一个MAP类型的缓存，本类作了改装 每个缓存区除包括缓存的实例外，还包括一个ID目录
 *
 * @author huajie.com
 * @param <T> 队列中保存的对象类型
 */
public class RereQueueCache<T> implements IRereQueueCache,Serializable{

    private final IRereCacheInstance cache;//存放缓存元素及元素目录的缓存区
    private LinkedBlockingQueue<String> catalog = new LinkedBlockingQueue();//存放缓存的目录队列，存取的顺序按照此队列中的顺序进行

    public RereQueueCache(IRereCacheInstance cache) {
        this.cache = cache;
        LinkedBlockingQueue o = (LinkedBlockingQueue) cache.fetchObject("catalog");
        if (o == null) {
            catalog = new LinkedBlockingQueue();
            cache.add("catalog", catalog);
        } else {
            catalog = o;
        }
    }

    private void refreshCatalog() {
        cache.update("catalog", catalog);
    }

    /**
     * 此方法从缓存队列中获得一个元素
     *
     * @return
     */
    @Override
    public T poll() {
        T value = null;
        String id = catalog.poll();//获得ID并被删除
        if (id != null) {
            value = (T) cache.fetchObject(id);
            cache.remove(id);//
            return value;
        }
        this.refreshCatalog();
        return value;
    }

    /**
     * 此方法从缓存队列中取出指定数据的元素
     *
     * @param num 取出的数量
     * @return
     */
    @Override
    public List<T> poll(int num) {
        List<String> ids = new ArrayList();
        List<T> list = new ArrayList();
        int i = 0;
        while (i < num) {
            i++;
            String id = catalog.poll();//获得ID并删除
            if (id != null) {
                ids.add(id);
            } else {
                break;
            }
        }
        for (String id : ids) {
            if (id != null) {
                T value = (T) cache.fetchObject(id);
                if (value != null) {
                    list.add(value);
                    cache.remove(id);//取出后从缓存中删除此元素
                }
            }
        }
        this.refreshCatalog();
        return list;
    }

    /**
     * 向队列中添加一个元素
     *
     * @param id 元素id
     * @param value 元素
     */
    @Override
    public void put(String id, Object value) {
        try {
            catalog.put(id);
            cache.add(id, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.refreshCatalog();
    }

    @Override
    public T find(String id) {
        if (!catalog.contains(id)) {
            return null;
        } else {
            T value = (T) cache.fetchObject(id);
            return value;
        }
    }

    /**
     * 获得当前缓存队列中的元素个数
     *
     * @return
     */
    @Override
    public int size() {
        return this.catalog.size();
    }

    @Override
    public void clear() {
        this.cache.removeAll();
        catalog = new LinkedBlockingQueue();
        cache.add("catalog", catalog);
    }

}
