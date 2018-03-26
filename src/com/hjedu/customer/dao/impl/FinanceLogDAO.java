
package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.dao.IFinanceLogDAO;
import com.hjedu.customer.entity.FinanceLog;

public class FinanceLogDAO implements IFinanceLogDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addFinanceLog(FinanceLog partnerModel) {
        entityManager.persist(partnerModel);
    }

    @Override
    public void deleteFinanceLog(String id) {
        FinanceLog cm = entityManager.find(FinanceLog.class, id);
        entityManager.remove(cm);
    }

    @Override
    public List<FinanceLog> findAllFinanceLog() {
        String q = "Select cms from FinanceLog cms order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setMaxResults(1000);
        List<FinanceLog> as = query.getResultList();
        return as;
    }

    @Override
    public List<FinanceLog> findFinanceLogByUsr(final String uid) {
        String q = "Select cms from FinanceLog cms where cms.user.id=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        query.setMaxResults(1000);
        List<FinanceLog> as = query.getResultList();
        return as;
    }

    @Override
    public FinanceLog findFinanceLog(String id) {
        return entityManager.find(FinanceLog.class, id);

    }

    @Override
    public void updateFinanceLog(FinanceLog comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from FinanceLog cms";
        entityManager.createQuery(q).executeUpdate();
    }
    
    @Override
    public void deleteLogByUser(String lid) {
        String q = "delete from FinanceLog cms where cms.user.id=:lid";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("lid", lid);
        qu.executeUpdate();
    }
}
