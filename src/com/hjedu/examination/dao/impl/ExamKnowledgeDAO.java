package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamKnowledgeDAO;
import com.hjedu.examination.dao.IWrongKnowledgeDAO;
import com.hjedu.examination.entity.ExamKnowledge;

public class ExamKnowledgeDAO implements IExamKnowledgeDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    private IWrongKnowledgeDAO wkDAO;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IWrongKnowledgeDAO getWkDAO() {
        return wkDAO;
    }

    public void setWkDAO(IWrongKnowledgeDAO wkDAO) {
        this.wkDAO = wkDAO;
    }

    @Override
    public void addExamKnowledge(ExamKnowledge m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamKnowledge(String id) {
        ExamKnowledge c = this.entityManager.find(ExamKnowledge.class, id);
        this.entityManager.remove(c);
        this.wkDAO.deleteWrongKnowledgeByKnowledge(id);
    }

    @Override
    public List<ExamKnowledge> findAllExamKnowledge() {
        String q = "Select cq from ExamKnowledge cq order by cq.genTime desc";
        List<ExamKnowledge> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ExamKnowledge> findExamKnowledgeByModule(String id) {
        String q = "Select cq from ExamKnowledge cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<ExamKnowledge> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public long getKnowledgeNumByModule(String id) {
        String q = "Select count(cq) from ExamKnowledge cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public ExamKnowledge findExamKnowledge(String id) {
        ExamKnowledge c = this.entityManager.find(ExamKnowledge.class, id);
        return c;
    }

    @Override
    public void updateExamKnowledge(ExamKnowledge m) {
        this.entityManager.merge(m);
    }

    @Override
    public void deleteExamKnowledgeByModule(String id) {
        List<ExamKnowledge> jqs = this.findExamKnowledgeByModule(id);
        for (ExamKnowledge j : jqs) {
            this.deleteExamKnowledge(j.getId());
        }
    }
    
    
    
}
