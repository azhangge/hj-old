package com.hjedu.customer.dao.impl;


import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IBbsScoreLogDAO;
import com.hjedu.platform.entity.BbsScoreLog;

public class BbsScoreLogDAO implements IBbsScoreLogDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addBbsScoreLog(BbsScoreLog partnerModel) {
        entityManager.persist(partnerModel);
    }

    public void deleteBbsScoreLog(String id) {
        BbsScoreLog cm = entityManager.find(BbsScoreLog.class, id);
        entityManager.remove(cm);
    }

    public List<BbsScoreLog> findAllBbsScoreLog() {
        String q = "Select cms from BbsScoreLog cms order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setMaxResults(1000);
        List<BbsScoreLog> as = query.getResultList();
        return as;
    }

    @Override
    public List<BbsScoreLog> findBbsScoreLogByUsr(final String uid) {
        String q = "Select cms from BbsScoreLog cms where cms.user.id=:uid order by cms.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", uid);
        qu.setMaxResults(1000);
        List<BbsScoreLog> as = qu.getResultList();
        return as;
    }

    public BbsScoreLog findBbsScoreLog(String id) {
        return entityManager.find(BbsScoreLog.class, id);

    }

    public void updateBbsScoreLog(BbsScoreLog comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from BbsScoreLog cms";
        entityManager.createQuery(q).executeUpdate();
    }
    
    @Override
    public void deleteByUsr(String id) {
        String q = "delete from BbsScoreLog cms where cms.user.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.executeUpdate();
    }
}
