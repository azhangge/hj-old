package com.hjedu.examination.dao.impl;


import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.examination.dao.IExamContestDAO;
import com.hjedu.examination.entity.contest.ExamContestSession;

public class ExamContestDAO implements IExamContestDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamContest(ExamContestSession m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamContest(String id) {
        ExamContestSession c = this.entityManager.find(ExamContestSession.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<ExamContestSession> findAllExamContest() {
        String q = "Select cq from ExamContestSession cq where cq.id!='7' order by cq.genTime desc";
        List<ExamContestSession> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public List<ExamContestSession> findExamContestByLabel(String labelId) {
        String q = "Select cq from ExamContestSession cq where cq.ifShow=true and cq.labelStr like '%" + labelId + "%' order by cq.genTime desc";
        List<ExamContestSession> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ExamContestSession> findAllShowedExamContest() {
        String q = "Select cq from ExamContestSession cq where cq.ifShow=true and cq.id!='7' order by cq.genTime desc";
        List<ExamContestSession> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ExamContestSession findExamContest(String id) {
        ExamContestSession c = this.entityManager.find(ExamContestSession.class, id);
        return c;
    }

    @Override
    public void updateExamContest(ExamContestSession m) {
        this.entityManager.merge(m);
    }
}
