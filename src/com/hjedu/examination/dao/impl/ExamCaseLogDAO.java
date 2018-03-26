package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamCaseLogDAO;
import com.hjedu.examination.entity.ExamCaseLog;

public class ExamCaseLogDAO implements IExamCaseLogDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ExamCaseLog> findAllExamCaseLog() {
        String q = "Select cq from ExamCaseLog cq  order by cq.genTime desc";
        List<ExamCaseLog> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ExamCaseLog> findExamCaseLogByUser(String userId, String examinationId) {
        String q = "Select cq from ExamCaseLog cq where cq.user.id=:uid and cq.examination.id=:eid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", userId);
        qu.setParameter("eid", examinationId);
        List<ExamCaseLog> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public ExamCaseLog findLatestExamCaseLogByUser(String userId, String examinationId) {
        String q = "Select cq from ExamCaseLog cq where cq.user.id=:uid and cq.examination.id=:eid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", userId);
        qu.setParameter("eid", examinationId);
        List<ExamCaseLog> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }
    
    @Override
    public long findNumExamCaseLogByUser(String userId, String examinationId) {
        String q = "Select cq from ExamCaseLog cq where cq.user.id=:uid and cq.examination.id=:eid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", userId);
        qu.setParameter("eid", examinationId);
        List<ExamCaseLog> cqs = qu.getResultList();
        return cqs.size();
    }

    @Override
    public ExamCaseLog findExamCaseLog(String id) {
        return this.entityManager.find(ExamCaseLog.class, id);
    }

    @Override
    public void addExamCaseLog(ExamCaseLog log) {
        this.entityManager.persist(log);
    }

    @Override
    public void deleteExamCaseLog(String id) {
        ExamCaseLog log = this.entityManager.find(ExamCaseLog.class, id);
        this.entityManager.remove(log);
    }

    @Override
    public void deleteAllExamCaseLog() {
        String q = "delete from ExamCaseLog cq";
        this.entityManager.createQuery(q).executeUpdate();
    }

    @Override
    public void deleteExamCaseLogByUser(String uid) {
        String q = "delete from ExamCaseLog cq where cq.user.id=:uid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", uid);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteExamCaseLogByExam(String eid) {
        String q = "delete from ExamCaseLog cq where cq.examination.id=:uid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", eid);
        qu.executeUpdate();
    }
}
