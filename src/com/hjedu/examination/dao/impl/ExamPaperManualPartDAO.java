package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamPaperManualPartDAO;
import com.hjedu.examination.entity.ExamPaperManualPart;

public class ExamPaperManualPartDAO implements IExamPaperManualPartDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamPaperManualPart(ExamPaperManualPart m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamPaperManualPart(String id) {
        ExamPaperManualPart c = this.entityManager.find(ExamPaperManualPart.class, id);
        if (c != null) {
            this.entityManager.remove(c);
        }
    }

    @Override
    public List<ExamPaperManualPart> findAllExamPaperManualPart() {
        String q = "Select cq from ExamPaperManualPart cq order by cq.genTime desc";
        List<ExamPaperManualPart> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ExamPaperManualPart> findAllExamPaperManualPartByPaper(String pid) {
        String q = "Select cq from ExamPaperManualPart cq where cq.paper.id=:pid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("pid", pid);
        List<ExamPaperManualPart> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public ExamPaperManualPart findExamPaperManualPart(String id) {
        ExamPaperManualPart c = this.entityManager.find(ExamPaperManualPart.class, id);
        return c;
    }

    @Override
    public void updateExamPaperManualPart(ExamPaperManualPart m) {
        this.entityManager.merge(m);
    }
}
