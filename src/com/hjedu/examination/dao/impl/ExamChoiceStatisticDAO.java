package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamChoiceStatisticDAO;
import com.hjedu.examination.entity.ExamChoiceStatistic;

public class ExamChoiceStatisticDAO implements IExamChoiceStatisticDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamChoiceStatistic(ExamChoiceStatistic m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamChoiceStatistic(String id) {
        ExamChoiceStatistic c = this.entityManager.find(ExamChoiceStatistic.class, id);
        if (c != null) {
            this.entityManager.remove(c);
        }
    }

    @Override
    public List<ExamChoiceStatistic> findAllExamChoiceStatistic() {
        String q = "Select cq from ExamChoiceStatistic cq order by cq.genTime desc";
        List<ExamChoiceStatistic> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ExamChoiceStatistic findExamChoiceStatistic(String id) {
        ExamChoiceStatistic c = this.entityManager.find(ExamChoiceStatistic.class, id);
        return c;
    }

    @Override
    public List<ExamChoiceStatistic> findExamChoiceStatisticByChoice(String questionId) {
        String q = "Select cq from ExamChoiceStatistic cq where cq.choice.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", questionId);
        List<ExamChoiceStatistic> cqs = qu.getResultList();
        return cqs;
    }
    
    
    @Override
    public long getNumByChoiceAndExam(String id,String examId) {
        String q = "Select count(ms) from ExamChoiceStatistic ms where ms.choice.id=:id and ms.exam.id=:examId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setParameter("examId", examId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }
    
    @Override
    public void deleteStatisticByCase(String caseId) {
        String q = "delete from ExamChoiceStatistic ms where ms.examCaseId=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", caseId);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteStatisticByExam(String caseId) {
        String q = "delete from ExamChoiceStatistic ms where ms.exam.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", caseId);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteStatisticByUser(String caseId) {
        String q = "delete from ExamChoiceStatistic ms where ms.user.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", caseId);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteStatisticByChoice(String caseId) {
        String q = "delete from ExamChoiceStatistic ms where ms.choice.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", caseId);
        qu.executeUpdate();
    }

    @Override
    public void updateExamChoiceStatistic(ExamChoiceStatistic m) {
        this.entityManager.merge(m);
    }
}
