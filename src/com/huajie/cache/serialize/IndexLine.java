
package com.huajie.cache.serialize;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author huajie
 * 本类用于表示本地的索引行
 */
public class IndexLine {
    
    String id = UUID.randomUUID().toString();
    Object keyWord;//索引条目的关键字
    Set<String> ids = new ConcurrentSkipListSet<String>();//保存了关键字key对应的所有元素id
    
    
    
    
}
