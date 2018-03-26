package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamChoiceDAO;
import com.hjedu.examination.dao.IExamChoiceStatisticDAO;
import com.hjedu.examination.entity.ExamChoice;

public class ExamChoiceDAO implements IExamChoiceDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    private IExamChoiceStatisticDAO statisticDAO;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IExamChoiceStatisticDAO getStatisticDAO() {
        return statisticDAO;
    }

    public void setStatisticDAO(IExamChoiceStatisticDAO statisticDAO) {
        this.statisticDAO = statisticDAO;
    }

    @Override
    public void addExamChoice(ExamChoice m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamChoice(String id) {
        ExamChoice c = this.entityManager.find(ExamChoice.class, id);
        if (c != null) {
            this.statisticDAO.deleteStatisticByChoice(id);
            this.entityManager.remove(c);
        }
    }

    @Override
    public List<ExamChoice> findAllExamChoice() {
        String q = "Select cq from ExamChoice cq order by cq.genTime desc";
        List<ExamChoice> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ExamChoice findExamChoice(String id) {
        ExamChoice c = this.entityManager.find(ExamChoice.class, id);
        return c;
    }

    @Override
    public List<ExamChoice> findExamChoiceByQuestion(String questionId) {
        String q = "Select cq from ExamChoice cq where cq.question.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", questionId);
        List<ExamChoice> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateExamChoice(ExamChoice m) {
        this.entityManager.merge(m);
    }
}
