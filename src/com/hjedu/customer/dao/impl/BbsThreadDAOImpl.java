package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IBbsThreadDAO;
import com.hjedu.platform.entity.BbsThread;

public class BbsThreadDAOImpl implements IBbsThreadDAO, Serializable {
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
    public void topThread(String t) {
        BbsThread bt = this.entityManager.find(BbsThread.class, t);
        bt.setTop(true);
        entityManager.merge(bt);
    }

    @Override
    public void unTopThread(String t) {
        BbsThread bt = this.entityManager.find(BbsThread.class, t);
        bt.setTop(false);
        entityManager.merge(bt);
    }

    @Override
    public void lockThread(String t) {
        BbsThread bt = this.entityManager.find(BbsThread.class, t);
        bt.setIfLocked(true);
        entityManager.merge(bt);
    }

    @Override
    public void unlockThread(String t) {
        BbsThread bt = this.entityManager.find(BbsThread.class, t);
        bt.setIfLocked(false);
        entityManager.merge(bt);
    }

    @Override
    public long getTalksNumByThread(String id) {
        String q = "Select count(ms) from BbsTalk ms where ms.thread.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }

    public void save(BbsThread entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void delete(String id) {
        try {
            BbsThread entity = entityManager.getReference(BbsThread.class,
                    id);
            entityManager.remove(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }
    
    

    public BbsThread update(BbsThread entity) {
        try {
            BbsThread result = entityManager.merge(entity);
            return result;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public BbsThread findById(String id) {
        try {
            BbsThread instance = entityManager.find(BbsThread.class, id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<BbsThread> findByProperty(String propertyName,
            final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from BbsThread model where model."
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

    public List<BbsThread> findByTitle(Object title,
            int... rowStartIdxAndCount) {
        return findByProperty(TITLE, title, rowStartIdxAndCount);
    }

    public List<BbsThread> findByGenBy(Object genBy,
            int... rowStartIdxAndCount) {
        return findByProperty(GEN_BY, genBy, rowStartIdxAndCount);
    }

    public List<BbsThread> findByLastReplyBy(Object lastReplyBy,
            int... rowStartIdxAndCount) {
        return findByProperty(LAST_REPLY_BY, lastReplyBy, rowStartIdxAndCount);
    }

    public List<BbsThread> findByZone(String id) {
        final String queryString = "select model from BbsThread model where model.zone.id=:id order by model.top desc,model.latestTalk.genTime desc";
        Query qu = this.entityManager.createQuery(queryString);
        qu.setParameter("id", id);
        List<BbsThread> ts = qu.getResultList();
        return ts;
    }

    public List<BbsThread> findByReadCount(Object readCount,
            int... rowStartIdxAndCount) {
        return findByProperty(READ_COUNT, readCount, rowStartIdxAndCount);
    }

    @SuppressWarnings("unchecked")
    public List<BbsThread> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from BbsThread model order by model.top desc,model.latestTalk.genTime desc";
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