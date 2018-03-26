package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IBbsTalkDAO;
import com.hjedu.platform.dao.IBbsThreadDAO;
import com.hjedu.platform.entity.BbsTalk;
import com.hjedu.platform.entity.BbsThread;

public class BbsTalkDAOImpl implements IBbsTalkDAO, Serializable {
    // property constants

    @PersistenceContext
    private EntityManager entityManager;
    private IBbsThreadDAO threadDAO;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IBbsThreadDAO getThreadDAO() {
        return threadDAO;
    }

    public void setThreadDAO(IBbsThreadDAO threadDAO) {
        this.threadDAO = threadDAO;
    }
    public static final String CONTENT = "content";
    public static final String THREAD = "thread";
    public static final String GEN_BY = "genBy";
    public static final String IF_PUB = "ifPub";

    public void save(BbsTalk entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void delete(String id) {
        try {
            BbsTalk entity = entityManager.find(BbsTalk.class, id);
            String tid = entity.getThread().getId();
            BbsTalk e2 = null;
            //System.out.println(entity.getThread().getTalks().size());
            BbsThread bt = entity.getThread();
            bt.getTalks().remove(entity);
            //System.out.println(entity.getThread().getTalks().size());
            entityManager.remove(entity);
            //此处在删除THREAD的最一个TALK时很容易出错，如果TALK被删，则THREAD的LATESTTALK所关联的TALK没有了，只要一调用THREAD就会出错。因此应该先设置THREAD的LATESTTALK为NULL，然后再找出来相应的TALK设置上去。好复杂。
            bt.setLatestTalk(null);
            this.threadDAO.update(bt);
            this.alterThreadLatestTalk(tid);
            //System.out.println("talk removed.");
        } catch (Exception re) {
            re.printStackTrace();
        }
    }

    private void alterThreadLatestTalk(String id) {
        BbsThread thread = threadDAO.findById(id);
        BbsTalk bt = this.findLatestTalkByThread(id);
        thread.setLatestTalk(bt);
        //System.out.println(bt.getId());
        this.threadDAO.update(thread);
    }

    public BbsTalk update(BbsTalk entity) {
        try {
            BbsTalk result = entityManager.merge(entity);
            return result;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public BbsTalk findById(String id) {
        try {
            BbsTalk instance = entityManager.find(BbsTalk.class, id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<BbsTalk> findByProperty(String propertyName,
            final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from BbsTalk model where model."
                    + propertyName + "= :propertyValue order by model.genTime";
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

    public List<BbsTalk> findByContent(Object content,
            int... rowStartIdxAndCount) {
        return findByProperty(CONTENT, content, rowStartIdxAndCount);
    }

    public List<BbsTalk> findByThread(String id) {
        String sql = "select talks from BbsTalk talks where talks.thread.id=:id order by talks.genTime";
        Query qu = this.entityManager.createQuery(sql);
        qu.setParameter("id", id);
        return qu.getResultList();
    }

    @Override
    public BbsTalk findLatestTalkByThread(String id) {
        String sql = "select talks from BbsTalk talks where talks.thread.id=:id order by talks.genTime desc";
        Query qu = this.entityManager.createQuery(sql);
        qu.setParameter("id", id);
        List<BbsTalk> b = qu.getResultList();
        if (b.isEmpty()) {
            return null;
        } else {
            return b.get(0);
        }
    }

    public List<BbsTalk> findByGenBy(Object genBy, int... rowStartIdxAndCount) {
        return findByProperty(GEN_BY, genBy, rowStartIdxAndCount);
    }

    public List<BbsTalk> findByIfPub(Object ifPub, int... rowStartIdxAndCount) {
        return findByProperty(IF_PUB, ifPub, rowStartIdxAndCount);
    }

    @SuppressWarnings("unchecked")
    public List<BbsTalk> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from BbsTalk model order by model.genTime";
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