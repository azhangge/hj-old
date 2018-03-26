
package com.huajie.cache;

/**
 * 本类对CacheItem的生命阶段进行回调
 * @author huajie
 */
public interface CacheItemLifeListener {
    
    
    /**
     * 缓存条目销毁时调用此方法
     * @param item 
     */
    public void destroy(RereCacheItem item);
    
}
