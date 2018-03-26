package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.ISystemEmailBoxDAO;
import com.hjedu.platform.entity.SystemEmailBoxModel;

public class SystemEmailBoxDAO implements ISystemEmailBoxDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<SystemEmailBoxModel> findAllEmailBox() {
        String q = "Select ss from SystemEmailBoxModel ss";
        List<SystemEmailBoxModel> ais = entityManager.createQuery(q).getResultList();
        return ais;
    }

    public void synEmailBox(List<SystemEmailBoxModel> ss) {
        String q = "delete from SystemEmailBoxModel ss";
        entityManager.createQuery(q).executeUpdate();
        for (SystemEmailBoxModel s : ss) {
            this.entityManager.persist(s);
        }
    }
}
