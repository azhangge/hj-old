package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamLabelDAO;
import com.hjedu.examination.entity.ExamLabel;

public class ExamLabelDAO implements IExamLabelDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamLabel(ExamLabel m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamLabel(String id) {
        ExamLabel c = this.entityManager.find(ExamLabel.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<ExamLabel> findAllExamLabel() {
        String q = "Select cq from ExamLabel cq order by cq.ord desc";
        List<ExamLabel> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ExamLabel> findAllShowedExamLabel() {
        String q = "Select cq from ExamLabel cq where cq.ifShow=true order by cq.ord desc";
        List<ExamLabel> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ExamLabel> findAllShowedExamLabelByType(String typeId) {
        String q = "Select cq from ExamLabel cq where cq.ifShow=true and cq.labelType.id=:typeId order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("typeId", typeId);
        List<ExamLabel> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ExamLabel> findMostExamLabelByType(String typeId) {
        String q = "Select cq from ExamLabel cq where cq.ifShow=true and cq.labelType.id=:typeId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("typeId", typeId);
        qu.setMaxResults(5);
        List<ExamLabel> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public ExamLabel findExamLabel(String id) {
        ExamLabel c = this.entityManager.find(ExamLabel.class, id);
        return c;
    }

    @Override
    public void updateExamLabel(ExamLabel m) {
        this.entityManager.merge(m);
    }
}
