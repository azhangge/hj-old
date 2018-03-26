package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.context.ApplicationContext;

import com.hjedu.platform.dao.IDictProvinceDAO;
import com.hjedu.platform.entity.DictProvince;

public class DictProvinceDAO implements IDictProvinceDAO, Serializable {
    //property constants

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public static final String _SPROVNAME = "SProvname";
    public static final String _STYPE = "SType";
    public static final String _SSTATE = "SState";

    public void save(DictProvince entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void delete(DictProvince entity) {
        try {
            entity = entityManager.getReference(DictProvince.class, entity.getNProvid());
            entityManager.remove(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public DictProvince update(DictProvince entity) {
        try {
            DictProvince result = entityManager.merge(entity);
            return result;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public DictProvince findById(Integer id) {
        try {
            DictProvince instance = entityManager.find(DictProvince.class, id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<DictProvince> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from DictProvince model where model."
                    + propertyName + "= :propertyValue";
            Query query = entityManager.createQuery(queryString);
            query.setParameter("propertyValue", value);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
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

    public List<DictProvince> findBySProvname(Object SProvname, int... rowStartIdxAndCount) {
        return findByProperty(_SPROVNAME, SProvname, rowStartIdxAndCount);
    }

    public List<DictProvince> findBySType(Object SType, int... rowStartIdxAndCount) {
        return findByProperty(_STYPE, SType, rowStartIdxAndCount);
    }

    public List<DictProvince> findBySState(Object SState, int... rowStartIdxAndCount) {
        return findByProperty(_SSTATE, SState, rowStartIdxAndCount);
    }

    @SuppressWarnings("unchecked")
    public List<DictProvince> findAll(
            final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from DictProvince model";
            Query query = entityManager.createQuery(queryString);
            if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
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

    public static IDictProvinceDAO getFromApplicationContext(ApplicationContext ctx) {
        return (IDictProvinceDAO) ctx.getBean("DictProvinceDAO");
    }
}