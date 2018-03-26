package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.course.dao.IRereAskZoneDAO;
import com.hjedu.course.entity.RereAsk;
import com.hjedu.course.entity.RereAskZone;

public class RereAskZoneDAOImpl implements IRereAskZoneDAO, Serializable {
    // property constants

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ORDER_INDEX = "orderIndex";

    public void save(RereAskZone entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void delete(String id) {
        try {
            RereAskZone entity = entityManager.getReference(RereAskZone.class,
                    id);
            entityManager.remove(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public RereAskZone update(RereAskZone entity) {
        try {
            RereAskZone result = entityManager.merge(entity);
            return result;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public RereAskZone findById(String id) {
        try {
            RereAskZone instance = entityManager.find(RereAskZone.class, id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<RereAskZone> findByProperty(String propertyName,
            final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from RereAskZone model where model."
                    + propertyName + "= :propertyValue";
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

    public List<RereAskZone> findByName(Object name, int... rowStartIdxAndCount) {
        return findByProperty(NAME, name, rowStartIdxAndCount);
    }

    public List<RereAskZone> findByDescription(Object description,
            int... rowStartIdxAndCount) {
        return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
    }

    public List<RereAskZone> findByOrderIndex(Object orderIndex,
            int... rowStartIdxAndCount) {
        return findByProperty(ORDER_INDEX, orderIndex, rowStartIdxAndCount);
    }

    @SuppressWarnings("unchecked")
    public List<RereAskZone> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from RereAskZone model order by model.orderIndex";
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

    @Override
    public RereAsk findLatestAsk(final String id) {
        String q = "select bt from RereAsk bt where bt.zone.id=:id order by bt.latestTalk.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<RereAsk> ps = qu.getResultList();
        if (!ps.isEmpty()) {
            return ps.get(0);
        } else {
            return null;
        }
    }
}