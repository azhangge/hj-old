package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.IBbsScoreRegularDAO;
import com.hjedu.platform.entity.BbsScoreRegular;

public class BbsScoreRegularDAO implements IBbsScoreRegularDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BbsScoreRegular findScoreRegular() {
        String q = "Select ais from BbsScoreRegular ais";
        BbsScoreRegular ais = null;
        try {
            ais = (BbsScoreRegular) entityManager.createQuery(q).getSingleResult();
        } catch (Exception e) {
        }
        if (ais == null) {
            return new BbsScoreRegular();
        } else {
            return ais;
        }
    }

    @Override
    public void updateScoreRegular(BbsScoreRegular n) {
        this.entityManager.merge(n);
    }

}
