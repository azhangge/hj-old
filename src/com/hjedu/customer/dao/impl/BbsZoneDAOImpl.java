package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IBbsZoneDAO;
import com.hjedu.platform.entity.BbsThread;
import com.hjedu.platform.entity.BbsZone;

public class BbsZoneDAOImpl implements IBbsZoneDAO, Serializable {
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
    public static final String MANAGER = "manager";
    public static final String ORDER_INDEX = "orderIndex";

    public void save(BbsZone entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void delete(String id) {
        try {
            BbsZone entity = entityManager.getReference(BbsZone.class,
                    id);
            entityManager.remove(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public BbsZone update(BbsZone entity) {
        try {
            BbsZone result = entityManager.merge(entity);
            return result;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public BbsZone findById(String id) {
        try {
            BbsZone instance = entityManager.find(BbsZone.class, id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<BbsZone> findByProperty(String propertyName,
            final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from BbsZone model where model."
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

    public List<BbsZone> findByName(Object name, int... rowStartIdxAndCount) {
        return findByProperty(NAME, name, rowStartIdxAndCount);
    }

    public List<BbsZone> findByDescription(Object description,
            int... rowStartIdxAndCount) {
        return findByProperty(DESCRIPTION, description, rowStartIdxAndCount);
    }

    public List<BbsZone> findByManager(Object manager,
            int... rowStartIdxAndCount) {
        return findByProperty(MANAGER, manager, rowStartIdxAndCount);
    }

    public List<BbsZone> findByOrderIndex(Object orderIndex,
            int... rowStartIdxAndCount) {
        return findByProperty(ORDER_INDEX, orderIndex, rowStartIdxAndCount);
    }

    @SuppressWarnings("unchecked")
    public List<BbsZone> findAll(final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from BbsZone model order by model.orderIndex";
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
    public BbsThread findLatestThread(final String id) {
        String q = "select bt from BbsThread bt where bt.zone.id=:id order by bt.latestTalk.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<BbsThread> ps = qu.getResultList();
        if (!ps.isEmpty()) {
            return ps.get(0);
        } else {
            return null;
        }
    }
}