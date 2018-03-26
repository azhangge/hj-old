package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.context.ApplicationContext;

import com.hjedu.platform.dao.IDictCityDAO;
import com.hjedu.platform.entity.DictCity;

public class DictCityDAO implements IDictCityDAO, Serializable {
    //property constants

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public static final String _SCITYNAME = "SCityname";
    public static final String _NPROVID = "NProvid";
    public static final String _SSTATE = "SState";

    public void save(DictCity entity) {
        try {
            entityManager.persist(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public void delete(DictCity entity) {
        try {
            entity = entityManager.getReference(DictCity.class, entity.getNCityid());
            entityManager.remove(entity);
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public DictCity update(DictCity entity) {
        try {
            DictCity result = entityManager.merge(entity);
            return result;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    public DictCity findById(Integer id) {
        try {
            DictCity instance = entityManager.find(DictCity.class, id);
            return instance;
        } catch (RuntimeException re) {
            throw re;
        }
    }

    @SuppressWarnings("unchecked")
    public List<DictCity> findByProperty(String propertyName, final Object value, final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from DictCity model where model."
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

    public List<DictCity> findBySCityname(Object SCityname, int... rowStartIdxAndCount) {
        return findByProperty(_SCITYNAME, SCityname, rowStartIdxAndCount);
    }

    public List<DictCity> findByNProvid(Object NProvid, int... rowStartIdxAndCount) {
        return findByProperty(_NPROVID, NProvid, rowStartIdxAndCount);
    }

    public List<DictCity> findBySState(Object SState, int... rowStartIdxAndCount) {
        return findByProperty(_SSTATE, SState, rowStartIdxAndCount);
    }

    @SuppressWarnings("unchecked")
    public List<DictCity> findAll(
            final int... rowStartIdxAndCount) {
        try {
            final String queryString = "select model from DictCity model";
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

    public static IDictCityDAO getFromApplicationContext(ApplicationContext ctx) {
        return (IDictCityDAO) ctx.getBean("DictCityDAO");
    }
}