package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.IFireWallDAO;
import com.hjedu.platform.entity.FireWall;

public class FireWallDAO implements IFireWallDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void updateFireWall(FireWall m) {
        this.entityManager.merge(m);
    }

    @Override
    public FireWall findFireWall() {
        String q = "Select cq from FireWall cq";
        FireWall cqs = null;
        try {
            cqs = (FireWall) this.entityManager.createQuery(q).getSingleResult();
        } catch (Exception e) {
        }
        if (cqs == null) {
            cqs = new FireWall();
        }
        return cqs;
    }
}
