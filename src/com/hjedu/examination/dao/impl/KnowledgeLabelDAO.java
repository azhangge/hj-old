package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IKnowledgeLabelDAO;
import com.hjedu.examination.entity.KnowledgeLabel;

public class KnowledgeLabelDAO implements IKnowledgeLabelDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addKnowledgeLabel(KnowledgeLabel m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteKnowledgeLabel(String id) {
        KnowledgeLabel c = this.entityManager.find(KnowledgeLabel.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<KnowledgeLabel> findAllKnowledgeLabel() {
        String q = "Select cq from KnowledgeLabel cq order by cq.ord desc";
        List<KnowledgeLabel> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<KnowledgeLabel> findAllShowedKnowledgeLabel() {
        String q = "Select cq from KnowledgeLabel cq where cq.ifShow=true order by cq.ord desc";
        List<KnowledgeLabel> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<KnowledgeLabel> findAllShowedKnowledgeLabelByType(String typeId) {
        String q = "Select cq from KnowledgeLabel cq where cq.ifShow=true and cq.labelType.id=:typeId order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("typeId", typeId);
        List<KnowledgeLabel> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<KnowledgeLabel> findMostKnowledgeLabelByType(String typeId) {
        String q = "Select cq from KnowledgeLabel cq where cq.ifShow=true and cq.labelType.id=:typeId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("typeId", typeId);
        qu.setMaxResults(5);
        List<KnowledgeLabel> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public KnowledgeLabel findKnowledgeLabel(String id) {
        KnowledgeLabel c = this.entityManager.find(KnowledgeLabel.class, id);
        return c;
    }

    @Override
    public void updateKnowledgeLabel(KnowledgeLabel m) {
        this.entityManager.merge(m);
    }
}
