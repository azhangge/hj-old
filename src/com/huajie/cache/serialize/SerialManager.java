
package com.huajie.cache.serialize;

import com.huajie.cache.RereCacheItem;

/**
 *
 * @author huajie
 */
public class SerialManager {
    
    private static ICacheSerial serial;
    
    public static RereCacheItem findItem(String id) {
        return serial.findItem(id);
    }
    
    public static void writeItem(RereCacheItem item) {
        serial.writeItem(item);
    }
    
    public static void deleteItem(String id) {
        serial.deleteItem(id);
    }
    
    public static void checkLives(long liveLen){
    
    
    }
    
}
