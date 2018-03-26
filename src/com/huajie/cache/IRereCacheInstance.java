package com.huajie.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.huajie.cache.index.RereCacheIndexInstance;
import com.huajie.cache.serialize.ICacheSerial;

/**
 * 缓存实例类，其中可以装载很多缓存条目，一个缓存实例通常用于缓存一种类型的对象
 *
 * @author huajie.com
 */
public interface IRereCacheInstance<T> extends Serializable {
    
   
    /**
     * 修改一个缓存条目中的对象，若存在
     *
     * @param id
     * @param target
     */
    public void update(String id, T target);
    
    public void add(String id, Object target);

    /**
     * 直接获得一个缓存条目中的对象
     *
     * @param id
     * @return
     */
    public T fetchObject(String id) ;

    /**
     * 返回本缓存保存的所有缓存对象
     *
     * @return
     */
    public List<T> fetchAllObjects();

    /**
     * 按索引名称查询关键字对应的数据集
     *
     * @param indexName
     * @param keyWord
     * @return
     */
    public List<T> queryByIndexEqual(String indexName, String keyWord);

    /**
     * 按索引名称查询关键字对应的数据ID集
     *
     * @param indexName
     * @param keyWord
     * @return
     */
    public List<String> queryIdsByIndexEqual(String indexName, String keyWord);

    /**
     * 返回本缓存实例的目录
     *
     * @return
     */
    public Set fetchCatelog() ;

    /**
     *
     * @param id
     * @return 按ID返回一个标准RereCacheItem
     */
    public RereCacheItem get(String id);

    /**
     * 获取所有的条目
     *
     * @return
     */
    public List<RereCacheItem> getAll() ;

    /**
     * 按照id删除某条目
     *
     * @param id
     */
    public void remove(String id);

    /**
     * 生命到期后逐出条目，逐出时会触发生命周期回调
     *
     * @param id
     */
    public void evict(String id) ;

    /**
     * 删除所有条目
     */
    public void removeAll();
    
    public void batchRemove(List<String> ids);
    
    public void destroy();

    /**
     * 添加缓存条目生命周期回调接口
     *
     * @param li
     */
    public void addLifeListener(CacheItemLifeListener li);

    /**
     * 检查本缓存实例中的条目是否过期
     */
    public void checkLives();
    
    
    public long getLifeLen();
    
    public ICacheSerial getCacheSerial() ;
    
    public Map<String, RereCacheIndexInstance> getIndexes() ;
    
    public Map<String, RereCacheItem> getItems();
    
    /**
     * 设置本缓存实例生命时长，以秒为单位，若设为0，表示永生。
     *
     * @param lifeLen
     */
    public void setLifeLen(long lifeLen);
    
}
