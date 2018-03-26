/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamCheatDAO;
import com.hjedu.examination.entity.ExamCheat;

public class ExamCheatDAO implements IExamCheatDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamCheat(ExamCheat partnerModel) {
        entityManager.persist(partnerModel);
    }

    @Override
    public void deleteExamCheat(String id) {
        ExamCheat cm = entityManager.find(ExamCheat.class, id);
        entityManager.remove(cm);
    }

    @Override
    public List<ExamCheat> findAllExamCheat() {
        String q = "Select cms from ExamCheat cms order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setMaxResults(1000);
        List<ExamCheat> as = query.getResultList();
        return as;
    }

    @Override
    public List<ExamCheat> findExamCheatByUsr(final String uid) {
        String q = "Select cms from ExamCheat cms where cms.userId=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        query.setMaxResults(1000);
        List<ExamCheat> as = query.getResultList();
        return as;
    }
    
    @Override
    public List<ExamCheat> findExamCheatByExam(final String uid) {
        String q = "Select cms from ExamCheat cms where cms.examId=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        query.setMaxResults(1000);
        List<ExamCheat> as = query.getResultList();
        return as;
    }
    
    @Override
    public List<ExamCheat> findExamCheatByCase(final String uid) {
        String q = "Select cms from ExamCheat cms where cms.caseId=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        query.setMaxResults(1000);
        List<ExamCheat> as = query.getResultList();
        return as;
    }

    @Override
    public ExamCheat findExamCheat(String id) {
        return entityManager.find(ExamCheat.class, id);

    }

    @Override
    public void updateExamCheat(ExamCheat comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from ExamCheat cms";
        entityManager.createQuery(q).executeUpdate();
    }
}
