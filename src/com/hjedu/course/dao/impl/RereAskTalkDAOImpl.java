package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.course.dao.IRereAskDAO;
import com.hjedu.course.dao.IRereAskTalkDAO;
import com.hjedu.course.entity.RereAsk;
import com.hjedu.course.entity.RereAskTalk;

public class RereAskTalkDAOImpl implements IRereAskTalkDAO, Serializable {
    // property constants

    @PersistenceContext
    private EntityManager entityManager;
    private IRereAskDAO askDAO;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IRereAskDAO getAskDAO() {
        return askDAO;
    }

    public void setAskDAO(IRereAskDAO askDAO) {
        this.askDAO = askDAO;
    }

    
    public static final String CONTENT = "content";
    public static final String THREAD = "thread";
    public static final String GEN_BY = "genBy";
    public static final String IF_PUB = "ifPub";

    public void save(RereAskTalk entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void delete(String id) {
        try {
            RereAskTalk entity = entityManager.find(RereAskTalk.class, id);
            String tid = entity.getAsk().getId();
            RereAskTalk e2 = null;
            //System.out.println(entity.getAsk().getTalks().size());
            RereAsk bt = entity.getAsk();
            bt.getTalks().remove(entity);
            //System.out.println(entity.getAsk().getTalks().size());
            entityManager.remove(entity);
            //此处在删除THREAD的最一个TALK时很容易出错，如果TALK被删，则THREAD的LATESTTALK所关联的TALK没有了，只要一调用THREAD就会出错。因此应该先设置THREAD的LATESTTALK为NULL，然后再找出来相应的TALK设置上去。好复杂。
            bt.setLatestTalk(null);
            this.askDAO.update(bt);
            this.alterAskLatestTalk(tid);
            //System.out.println("talk removed.");
        } catch (Exception re) {
            re.printStackTrace();
        }
    }

    private void alterAskLatestTalk(String id) {
        RereAsk thread = askDAO.findById(id);
        RereAskTalk bt = this.findLatestTalkByAsk(id);
        thread.setLatestTalk(bt);
        //System.out.println(bt.getId());
        this.askDAO.update(thread);
    }

    public RereAskTalk update(RereAskTalk entity) {
        try {
            RereAskTalk result = entityManager.merge(entity);
            return result;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public RereAskTalk findById(String id) {
        try {
            RereAskTalk instance = entityManager.find(RereAskTalk.class, id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<RereAskTalk> findByProperty(String propertyName,
            final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from RereAskTalk model where model."
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

    public List<RereAskTalk> findByContent(Object content,
            int... rowStartIdxAndCount) {
        return findByProperty(CONTENT, content, rowStartIdxAndCount);
    }

    public List<RereAskTalk> findByAsk(String id) {
        String sql = "select talks from RereAskTalk talks where talks.thread.id=:id order by talks.genTime";
        Query qu = this.entityManager.createQuery(sql);
        qu.setParameter("id", id);
        return qu.getResultList();
    }

    @Override
    public RereAskTalk findLatestTalkByAsk(String id) {
        String sql = "select talks from RereAskTalk talks where talks.thread.id=:id order by talks.genTime desc";
        Query qu = this.entityManager.createQuery(sql);
        qu.setParameter("id", id);
        List<RereAskTalk> b = qu.getResultList();
        if (b.isEmpty()) {
            return null;
        } else {
            return b.get(0);
        }
    }

    public List<RereAskTalk> findByGenBy(Object genBy, int... rowStartIdxAndCount) {
        return findByProperty(GEN_BY, genBy, rowStartIdxAndCount);
    }

    public List<RereAskTalk> findByIfPub(Object ifPub, int... rowStartIdxAndCount) {
        return findByProperty(IF_PUB, ifPub, rowStartIdxAndCount);
    }

    @SuppressWarnings("unchecked")
    public List<RereAskTalk> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from RereAskTalk model order by model.genTime";
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