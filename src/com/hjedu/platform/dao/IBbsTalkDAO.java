package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.BbsTalk;


public interface IBbsTalkDAO {

    public void save(BbsTalk entity);

    public void delete(String id);

    public BbsTalk update(BbsTalk entity);

    public BbsTalk findById(String id);

    public List<BbsTalk> findByProperty(String propertyName, Object value,
            int... rowStartIdxAndCount);

    public List<BbsTalk> findByContent(Object content,
            int... rowStartIdxAndCount);

    public List<BbsTalk> findByThread(String id);

    public List<BbsTalk> findByGenBy(Object genBy, int... rowStartIdxAndCount);

    public List<BbsTalk> findByIfPub(Object ifPub, int... rowStartIdxAndCount);

    public List<BbsTalk> findAll(int... rowStartIdxAndCount);
    
    public BbsTalk findLatestTalkByThread(String id);
    
}