package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.IOnlinePayConfigDAO;
import com.hjedu.platform.entity.OnlinePayConfig;

public class OnlinePayConfigDAO implements IOnlinePayConfigDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void updateOnlinePayConfig(OnlinePayConfig m) {
        this.entityManager.merge(m);
    }

    @Override
    public OnlinePayConfig findOnlinePayConfig() {
        String q = "Select cq from OnlinePayConfig cq";
        OnlinePayConfig cqs = null;
        try {
            cqs = (OnlinePayConfig) this.entityManager.createQuery(q).getSingleResult();
        } catch (Exception e) {
        }
        if (cqs == null) {
            cqs = new OnlinePayConfig();
        }
        return cqs;
    }
}
