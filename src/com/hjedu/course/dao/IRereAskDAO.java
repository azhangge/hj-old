package com.hjedu.course.dao;

import java.util.List;

import com.hjedu.course.entity.RereAsk;

public interface IRereAskDAO {

    public void topAsk(String t);
    
    public void lockAsk(String t);
    
    public void unlockAsk(String t);

    public void unTopAsk(String t);
    
    public abstract long getTalksNumByAsk(String id);

    public void save(RereAsk entity);

    public void delete(String id);

    public RereAsk update(RereAsk entity);

    public RereAsk findById(String id);

    public List<RereAsk> findByProperty(String propertyName, Object value,
            int... rowStartIdxAndCount);

    public List<RereAsk> findByTitle(Object title,
            int... rowStartIdxAndCount);

    public List<RereAsk> findByGenBy(Object genBy,
            int... rowStartIdxAndCount);

    public List<RereAsk> findByLastReplyBy(Object lastReplyBy,
            int... rowStartIdxAndCount);

    public List<RereAsk> findByZone(String id);

    public List<RereAsk> findByReadCount(Object readCount,
            int... rowStartIdxAndCount);

    public List<RereAsk> findAll(int... rowStartIdxAndCount);
}