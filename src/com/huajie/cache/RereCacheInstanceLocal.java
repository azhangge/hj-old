package com.huajie.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.huajie.cache.index.RereCacheIndexInstance;
import com.huajie.cache.index.RereCacheIndexUtil;
import com.huajie.cache.serialize.CacheSerialBasic;
import com.huajie.cache.serialize.ICacheSerial;
import com.huajie.util.SpringHelper;

/**
 * 缓存实例类，其中可以装载很多缓存条目，一个缓存实例通常用于缓存一种类型的对象
 *
 * @author huajie.com
 */
public class RereCacheInstanceLocal<T> implements IRereCacheInstance {
    
    private String id = UUID.randomUUID().toString();
    private String name = "";
    //private String type = "map";//map || list
    private Map<String, RereCacheItem> items = new java.util.concurrent.ConcurrentSkipListMap<String, RereCacheItem>();
    private long lifeLen = 600;//该实例下条目的默认生命时长，以秒为单位，若设为0，表示永生。
    private long maxItemNum = SpringHelper.getSpringBean("max_cache_item_num");//最大缓存条目数

    private Map<String, RereCacheIndexInstance> indexes = new ConcurrentHashMap<String, RereCacheIndexInstance>();//缓存中数据对象的索引，以供查询使用
    private ICacheSerial cacheSerial;//用于将内存缓存进行持久化操作
    private List<CacheItemLifeListener> itemLifeListener = new ArrayList();//缓存条目生命周期回调监听器

    /**
     * 构造方法
     *
     * @param name
     */
    public RereCacheInstanceLocal(String name) {
        this.name = name;
        cacheSerial = new CacheSerialBasic(name);
    }

    /**
     * 加入一个缓存条目中的对象
     *
     * @param id
     * @param target
     */
    private void put(String id, Object target) {
        RereCacheItem ci = new RereCacheItem();
        ci.setId(id);
        ci.setObject(target);
        this.items.put(id, ci);
        RereCacheIndexUtil.checkIndexWithPut(this, target);
        this.checkUnloadItems();
    }
    
    private void put(RereCacheItem ci) {
        this.items.put(ci.getId(), ci);
    }
    
    public void add(String id, Object target) {
        this.put(id, target);
    }

    /**
     * 修改一个缓存条目中的对象，若存在
     *
     * @param id
     * @param target
     */
    public void update(String id, Object target) {
        RereCacheItem item = this.get(id);
        if (item != null) {
            item.setObject(target);
            item.setVisitTime(new Date());
            RereCacheIndexUtil.checkIndexWithPut(this, target);
        }
    }

    /**
     * 本方法用于检查内存缓存是否超出了最大条目数，如果超出，则从内存中卸载
     */
    private void checkUnloadItems() {
        if (items.size() > this.maxItemNum) {
            int unloadLen = (int) (this.lifeLen * 0.5);//生命周期高于50%的全部删除；
            long time = System.currentTimeMillis();
            Set<String> ids = items.keySet();
            for (String idd : ids) {
                RereCacheItem item = items.get(idd);//准备被移出的条目
                long interval = time - item.getVisitTime().getTime();//计算条目从上次访问到当前的时间间隔
                if (interval > unloadLen * 1000) {//如果生命周期已到，则删除此条目
                    //序列化此缓存条目
                    cacheSerial.writeItem(item);
                    //RereCacheIndexUtil.checkIndexWithDelete(this, idd);//从索引中删除本ID
                    items.remove(idd);//从缓存实例中删除条目
                }
            }
        }
    }

    /**
     * 直接获得一个缓存条目中的对象
     *
     * @param id
     * @return
     */
    public T fetchObject(String id) {
        RereCacheItem<T> ci = items.get(id);
        if (ci != null) {//先从内存缓存查找
            ci.setVisitTime(new Date());
            return ci.getObject();
        } else {//若内存中不存在，则从序列化缓存查找
            ci = cacheSerial.findItem(id);
            if (ci != null) {
                items.put(id, ci);//从本地提取后，将放入内存
                cacheSerial.deleteItem(id);//加载到内存中后，将原持久化的数据删除
                return ci.getObject();
            }
        }
        return null;
    }

    /**
     * 返回本缓存保存的所有缓存对象
     *
     * @return
     */
    public List<T> fetchAllObjects() {
        Set<T> set = new HashSet();
        Set<String> ids = this.items.keySet();
        for (String idd : ids) {
            RereCacheItem<T> ci = items.get(idd);
            if (ci != null) {
                ci.setVisitTime(new Date());
                set.add(ci.getObject());
            }
        }
        //加载被持久化的所有缓存数据
        List<RereCacheItem> cs = this.cacheSerial.findAllItems();
        for (RereCacheItem<T> ci : cs) {
            if (ci != null) {
                set.add(ci.getObject());
            }
        }
        List<T> list = new ArrayList();
        list.addAll(set);
        return list;
    }

    /**
     * 按索引名称查询关键字对应的数据集
     *
     * @param indexName
     * @param keyWord
     * @return
     */
    public List<T> queryByIndexEqual(String indexName, String keyWord) {
        Set<T> set = new HashSet();
        Set<String> ids = RereCacheIndexUtil.queryIds(this, indexName, keyWord);
        if (ids != null) {
            for (String idd : ids) {
                T oo = this.fetchObject(idd);
                if (oo != null) {
                    set.add(oo);
                }
            }
        }
        List<T> list = new ArrayList();
        list.addAll(set);
        return list;
    }

    /**
     * 按索引名称查询关键字对应的数据ID集
     *
     * @param indexName
     * @param keyWord
     * @return
     */
    public List<String> queryIdsByIndexEqual(String indexName, String keyWord) {
        Set<String> ids = RereCacheIndexUtil.queryIds(this, indexName, keyWord);
        List<String> list = new ArrayList();
        list.addAll(ids);
        return list;
    }

    /**
     * 返回本缓存实例的目录
     *
     * @return
     */
    public Set fetchCatelog() {
        return this.items.keySet();
    }

    /**
     *
     * @param id
     * @return 按ID返回一个标准RereCacheItem
     */
    public RereCacheItem get(String id) {
        RereCacheItem<T> ci = items.get(id);
        if (ci != null) {
            ci.setVisitTime(new Date());
            return ci;
        } else {
            return null;
        }
    }

    /**
     * 获取所有的条目
     *
     * @return
     */
    public List<RereCacheItem> getAll() {
        List<RereCacheItem> list = new ArrayList();
        Collection<RereCacheItem> values = this.items.values();
        list.addAll(values);
        return list;
    }

    /**
     * 按照id删除某条目
     *
     * @param id
     */
    public void remove(String id) {
        RereCacheIndexUtil.checkIndexWithDelete(this, id);//删除条目时应首先更新索引
        items.remove(id);
    }

    /**
     * 生命到期后逐出条目，逐出时会触发生命周期回调
     *
     * @param id
     */
    public void evict(String id) {
        RereCacheIndexUtil.checkIndexWithDelete(this, id);//删除条目时应首先更新索引
        try {
            for (CacheItemLifeListener li : this.itemLifeListener) {
                li.destroy(this.get(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        items.remove(id);
    }

    /**
     * 删除所有条目
     */
    public void removeAll() {
        this.indexes.clear();//清除索引
        this.items.clear();//清除数据条目
    }

    @Override
    public void batchRemove(List ids) {
        if (ids != null) {
            for (Object idd : ids) {
                this.remove(idd.toString());
            }
        }
    }

    /**
     * 添加缓存条目生命周期回调接口
     *
     * @param li
     */
    public void addLifeListener(CacheItemLifeListener li) {
        this.getItemLifeListener().add(li);
    }

    /**
     * 检查本缓存实例中的条目是否过期
     */
    @Override
    public void checkLives() {
        //检查内存中的实例
        long time = System.currentTimeMillis();
        Map<String, RereCacheItem> itemsMap = this.getItems();//缓存实例中的所有条目
        Set<String> ids = itemsMap.keySet();
        for (String idd : ids) {
            RereCacheItem item = itemsMap.get(idd);
            long interval = time - item.getVisitTime().getTime();//计算条目从上次访问到当前的时间间隔
            if (this.getLifeLen() != 0 && interval > this.getLifeLen() * 1000) {//如果缓存实例非要求条目永生，并且生命周期已到，则删除此条目
                this.evict(id);
            }
        }
    }
    
    @Override
    public void destroy() {
        RereCacheManager.removeInstance(name);
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
    
    public Map<String, RereCacheItem> getItems() {
        return items;
    }
    
    public void setItems(Map<String, RereCacheItem> items) {
        this.items = items;
    }
    
    public long getLifeLen() {
        return lifeLen;
    }
    
    public ICacheSerial getCacheSerial() {
        return cacheSerial;
    }
    
    public void setCacheSerial(ICacheSerial cacheSerial) {
        this.cacheSerial = cacheSerial;
    }

    /**
     * 设置本缓存实例生命时长，以秒为单位，若设为0，表示永生。
     *
     * @param lifeLen
     */
    public void setLifeLen(long lifeLen) {
        this.lifeLen = lifeLen;
    }
    
    public long getMaxItemNum() {
        return maxItemNum;
    }
    
    public void setMaxItemNum(long maxItemNum) {
        this.maxItemNum = maxItemNum;
    }
    
    public Map<String, RereCacheIndexInstance> getIndexes() {
        return indexes;
    }
    
    public void setIndexes(Map<String, RereCacheIndexInstance> indexes) {
        this.indexes = indexes;
    }
    
    public List<CacheItemLifeListener> getItemLifeListener() {
        return itemLifeListener;
    }
    
    public void setItemLifeListener(List<CacheItemLifeListener> itemLifeListener) {
        this.itemLifeListener = itemLifeListener;
    }
    
    public static void main(String args[]) {
        RereCacheInstanceLocal ci = new RereCacheInstanceLocal("temp");
        ci.put("test", "111");
    }
    
}
