package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.examination.dao.IKnowledgeLabelTypeDAO;
import com.hjedu.examination.entity.KnowledgeLabelType;

public class KnowledgeLabelTypeDAO implements IKnowledgeLabelTypeDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addKnowledgeLabelType(KnowledgeLabelType paramKnowledgeLabelType) {
        this.entityManager.persist(paramKnowledgeLabelType);
    }

    @Override
    public void deleteKnowledgeLabelType(String paramString) {
        KnowledgeLabelType yp = (KnowledgeLabelType) this.entityManager.find(KnowledgeLabelType.class, paramString);
        this.entityManager.remove(yp);
    }

    @Override
    public List<KnowledgeLabelType> findAllKnowledgeLabelType() {
        String q = "select yis from KnowledgeLabelType yis order by yis.ord";
        List<KnowledgeLabelType> ps = entityManager.createQuery(q).getResultList();
        return ps;
    }

    @Override
    public KnowledgeLabelType findKnowledgeLabelType(String paramString) {
        KnowledgeLabelType p = (KnowledgeLabelType) this.entityManager.find(KnowledgeLabelType.class, paramString);
        return p;
    }

    @Override
    public void updateKnowledgeLabelType(KnowledgeLabelType paramKnowledgeLabelType) {
        this.entityManager.merge(paramKnowledgeLabelType);
    }
}
