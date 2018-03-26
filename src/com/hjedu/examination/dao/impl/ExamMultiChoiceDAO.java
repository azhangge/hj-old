package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamMultiChoiceDAO;
import com.hjedu.examination.entity.ExamMultiChoice;

public class ExamMultiChoiceDAO implements IExamMultiChoiceDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamMultiChoice(ExamMultiChoice m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamMultiChoice(String id) {
        ExamMultiChoice c = this.entityManager.find(ExamMultiChoice.class, id);
        if (c != null) {
            this.entityManager.remove(c);
        }
    }

    @Override
    public List<ExamMultiChoice> findAllExamMultiChoice() {
        String q = "Select cq from ExamMultiChoice cq order by cq.genTime desc";
        List<ExamMultiChoice> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ExamMultiChoice findExamMultiChoice(String id) {
        ExamMultiChoice c = this.entityManager.find(ExamMultiChoice.class, id);
        return c;
    }

    @Override
    public List<ExamMultiChoice> findExamMultiChoiceByQuestion(String id) {
        String q = "Select cq from ExamMultiChoice cq where cq.question.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<ExamMultiChoice> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateExamMultiChoice(ExamMultiChoice m) {
        this.entityManager.merge(m);
    }
}
