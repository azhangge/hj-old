
package com.huajie.cache;

import java.util.List;

/**
 *本类为一个缓存队列的接口，实现类可能是内存双队列缓存、Ehcache、或写入本地硬盘的缓存
 * @author huajie.com
 * @param <T> 队列中保存的对象类型
 */
public interface IRereQueueCache<T> {

    /**
     * 此方法从缓存队列中获得一个元素
     *
     * @return
     */
    public T poll();

    /**
     * 此方法从缓存队列中取出指定数据的元素
     *
     * @param num 取出的数量
     * @return
     */
    public List<T> poll(int num);

    /**
     * 向队列中添加一个元素
     * @param id 元素id
     * @param value 元素
     */
    public void put(String id, Object value);

    /**
     * 获得当前缓存队列中的元素个数
     *
     * @return
     */
    public int size();
    
    /**
     * 清除所有
     */
    public void clear();
    
    /**
     * 查询并获取一个对象
     * @param id 保存的key
     * @return 
     */
    public T find(String id);

}
