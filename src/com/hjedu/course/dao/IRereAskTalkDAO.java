package com.hjedu.course.dao;

import java.util.List;

import com.hjedu.course.entity.RereAskTalk;


public interface IRereAskTalkDAO {

    public void save(RereAskTalk entity);

    public void delete(String id);

    public RereAskTalk update(RereAskTalk entity);

    public RereAskTalk findById(String id);

    public List<RereAskTalk> findByProperty(String propertyName, Object value,
            int... rowStartIdxAndCount);

    public List<RereAskTalk> findByContent(Object content,
            int... rowStartIdxAndCount);

    public List<RereAskTalk> findByAsk(String id);

    public List<RereAskTalk> findByGenBy(Object genBy, int... rowStartIdxAndCount);

    public List<RereAskTalk> findByIfPub(Object ifPub, int... rowStartIdxAndCount);

    public List<RereAskTalk> findAll(int... rowStartIdxAndCount);
    
    public RereAskTalk findLatestTalkByAsk(String id);
    
}