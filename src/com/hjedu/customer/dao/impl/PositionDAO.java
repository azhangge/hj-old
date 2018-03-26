package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.IPositionDAO;
import com.hjedu.platform.entity.BbsPosition;

public class PositionDAO implements IPositionDAO ,Serializable{

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void updateBbsPosition(BbsPosition ca) {
        this.entityManager.merge(ca);
    }

    public BbsPosition findBbsPosition(String id) {
        return this.entityManager.find(BbsPosition.class, id);
    }

    public List<BbsPosition> findAllBbsPosition() {
        String q = "Select cs from BbsPosition cs order by cs.ord";
        List<BbsPosition> ais = this.entityManager.createQuery(q).getResultList();
        return ais;
    }

    public void deleteBbsPosition(String id) {
        BbsPosition p = this.entityManager.find(BbsPosition.class, id);
        this.entityManager.remove(p);
    }

    public void addBbsPosition(BbsPosition ca) {
        this.entityManager.persist(ca);
    }
}
