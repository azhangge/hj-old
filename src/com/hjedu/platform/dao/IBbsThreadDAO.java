package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.BbsThread;

public interface IBbsThreadDAO {

    public void topThread(String t);
    
    public void lockThread(String t);
    
    public void unlockThread(String t);

    public void unTopThread(String t);
    
    public abstract long getTalksNumByThread(String id);

    public void save(BbsThread entity);

    public void delete(String id);

    public BbsThread update(BbsThread entity);

    public BbsThread findById(String id);

    public List<BbsThread> findByProperty(String propertyName, Object value,
            int... rowStartIdxAndCount);

    public List<BbsThread> findByTitle(Object title,
            int... rowStartIdxAndCount);

    public List<BbsThread> findByGenBy(Object genBy,
            int... rowStartIdxAndCount);

    public List<BbsThread> findByLastReplyBy(Object lastReplyBy,
            int... rowStartIdxAndCount);

    public List<BbsThread> findByZone(String id);

    public List<BbsThread> findByReadCount(Object readCount,
            int... rowStartIdxAndCount);

    public List<BbsThread> findAll(int... rowStartIdxAndCount);
}