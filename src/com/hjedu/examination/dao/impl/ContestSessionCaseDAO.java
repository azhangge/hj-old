package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IContestSessionCaseDAO;
import com.hjedu.examination.entity.contest.ContestSessionCase;

public class ContestSessionCaseDAO implements IContestSessionCaseDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addContestSessionCase(ContestSessionCase m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteContestSessionCase(String id) {
        ContestSessionCase c = this.entityManager.find(ContestSessionCase.class, id);

        this.entityManager.remove(c);
    }

    @Override
    public List<ContestSessionCase> findAllContestSessionCase() {
        String q = "Select cq from ContestSessionCase cq where cq.examination.id!='7' order by cq.genTime desc";
        List<ContestSessionCase> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ContestSessionCase findContestSessionCase(String id) {
        ContestSessionCase c = this.entityManager.find(ContestSessionCase.class, id);
        return c;
    }

    @Override
    public List<ContestSessionCase> findContestSessionCaseByContest(String id) {
        String q = "Select cq from ContestSessionCase cq where cq.examination.id=:qid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ContestSessionCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public ContestSessionCase findContestSessionCaseByContestAndSessionStr(String id, String str) {
        String q = "Select cq from ContestSessionCase cq where cq.examination.id=:qid and cq.sessionStr=:str order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        qu.setParameter("str", str);
        List<ContestSessionCase> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    @Override
    public List<ContestSessionCase> findUnSubmittedContestSessionCaseByContest(String id) {
        String q = "Select cq from ContestSessionCase cq where cq.examination.id=:qid and cq.stat!='submitted' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ContestSessionCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ContestSessionCase> findSubmittedContestSessionCaseByContest(String id) {
        String q = "Select cq from ContestSessionCase cq where cq.examination.id=:qid and cq.stat='submitted' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ContestSessionCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public long getContestSessionCaseNum() {
        String q = "Select count(ms) from ContestSessionCase ms where ms.examination.id !='7'";
        Query qu = this.entityManager.createQuery(q);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public void updateContestSessionCase(ContestSessionCase m) {
        this.entityManager.merge(m);
    }

    @Override
    public long getParticipateNumByContest(String examId) {
        String q = "Select count(ms) from ContestSessionCase ms where ms.examination.id=:examId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    public void deleteAllContestSessionCase() {
        String q = "delete from ContestSessionCase cq";
        this.entityManager.createQuery(q).executeUpdate();
    }

}
