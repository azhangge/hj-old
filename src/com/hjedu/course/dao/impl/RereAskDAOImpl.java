package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.course.dao.IRereAskDAO;
import com.hjedu.course.entity.RereAsk;

public class RereAskDAOImpl implements IRereAskDAO, Serializable {
    // property constants

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public static final String TITLE = "title";
    public static final String GEN_BY = "genBy";
    public static final String LAST_REPLY_BY = "lastReplyBy";
    public static final String ZONE = "zone";
    public static final String READ_COUNT = "readCount";

    @Override
    public void topAsk(String t) {
        RereAsk bt = this.entityManager.find(RereAsk.class, t);
        bt.setTop(true);
        entityManager.merge(bt);
    }

    @Override
    public void unTopAsk(String t) {
        RereAsk bt = this.entityManager.find(RereAsk.class, t);
        bt.setTop(false);
        entityManager.merge(bt);
    }

    @Override
    public void lockAsk(String t) {
        RereAsk bt = this.entityManager.find(RereAsk.class, t);
        bt.setIfLocked(true);
        entityManager.merge(bt);
    }

    @Override
    public void unlockAsk(String t) {
        RereAsk bt = this.entityManager.find(RereAsk.class, t);
        bt.setIfLocked(false);
        entityManager.merge(bt);
    }

    @Override
    public long getTalksNumByAsk(String id) {
        String q = "Select count(ms) from BbsTalk ms where ms.thread.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }

    public void save(RereAsk entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void delete(String id) {
        try {
            RereAsk entity = entityManager.getReference(RereAsk.class,
                    id);
            entityManager.remove(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }
    
    

    public RereAsk update(RereAsk entity) {
        try {
            RereAsk result = entityManager.merge(entity);
            return result;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public RereAsk findById(String id) {
        try {
            RereAsk instance = entityManager.find(RereAsk.class, id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<RereAsk> findByProperty(String propertyName,
            final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from RereAsk model where model."
                    + propertyName + "= :propertyValue order by model.top desc,model.latestTalk.genTime desc";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            if (rowStartIdxAndCount != null
                    && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }

                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public List<RereAsk> findByTitle(Object title,
            int... rowStartIdxAndCount) {
        return findByProperty(TITLE, title, rowStartIdxAndCount);
    }

    public List<RereAsk> findByGenBy(Object genBy,
            int... rowStartIdxAndCount) {
        return findByProperty(GEN_BY, genBy, rowStartIdxAndCount);
    }

    public List<RereAsk> findByLastReplyBy(Object lastReplyBy,
            int... rowStartIdxAndCount) {
        return findByProperty(LAST_REPLY_BY, lastReplyBy, rowStartIdxAndCount);
    }

    public List<RereAsk> findByZone(String id) {
        final String queryString = "select model from RereAsk model where model.zone.id=:id order by model.top desc,model.latestTalk.genTime desc";
        Query qu = this.entityManager.createQuery(queryString);
        qu.setParameter("id", id);
        List<RereAsk> ts = qu.getResultList();
        return ts;
    }

    public List<RereAsk> findByReadCount(Object readCount,
            int... rowStartIdxAndCount) {
        return findByProperty(READ_COUNT, readCount, rowStartIdxAndCount);
    }

    @SuppressWarnings("unchecked")
    public List<RereAsk> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from RereAsk model order by model.top desc,model.latestTalk.genTime desc";
            Query query = entityManager.createQuery(queryString);
            if (rowStartIdxAndCount != null
                    && rowStartIdxAndCount.length > 0) {
                int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
                if (rowStartIdx > 0) {
                    query.setFirstResult(rowStartIdx);
                }

                if (rowStartIdxAndCount.length > 1) {
                    int rowCount = Math.max(0, rowStartIdxAndCount[1]);
                    if (rowCount > 0) {
                        query.setMaxResults(rowCount);
                    }
                }
            }
            return query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
    }
}