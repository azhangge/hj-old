/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huajie.cache.serialize;

import java.util.List;

import com.huajie.cache.RereCacheItem;

/**
 *
 * @author huajie
 */
public interface ICacheSerial {
    
    public RereCacheItem findItem(String id);
    public void writeItem(RereCacheItem item);
    public void deleteItem(String id);
    public void checkLives(long liveLen);
    public List<RereCacheItem> findAllItems();
}
