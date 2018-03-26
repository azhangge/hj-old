package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.IManualDAO;
import com.hjedu.platform.entity.ManualModel;

public class ManualDAO implements IManualDAO ,Serializable{

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void updateManual(ManualModel ca) {
        this.entityManager.merge(ca);
    }

    public ManualModel findManual(String id) {
        return this.entityManager.find(ManualModel.class, id);
    }

    public List<ManualModel> findAllManual() {
        String q = "Select cs from ManualModel cs order by cs.ord, cs.inputdate desc";
        List<ManualModel> ais = this.entityManager.createQuery(q).getResultList();
        return ais;
    }

    public void deleteManual(String id) {
        ManualModel p = this.entityManager.find(ManualModel.class, id);
        this.entityManager.remove(p);
    }

    public void addManual(ManualModel ca) {
        this.entityManager.persist(ca);
    }
}
