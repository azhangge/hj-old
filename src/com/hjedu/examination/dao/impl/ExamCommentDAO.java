
package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamCommentDAO;
import com.hjedu.examination.entity.ExamComment;

public class ExamCommentDAO implements IExamCommentDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamComment(ExamComment partnerModel) {
        entityManager.persist(partnerModel);
    }

    @Override
    public void deleteExamComment(String id) {
        ExamComment cm = entityManager.find(ExamComment.class, id);
        entityManager.remove(cm);
    }

    @Override
    public List<ExamComment> findAllExamComment() {
        String q = "Select cms from ExamComment cms order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setMaxResults(1000);
        List<ExamComment> as = query.getResultList();
        return as;
    }

    @Override
    public List<ExamComment> findExamCommentByUsr(final String uid) {
        String q = "Select cms from ExamComment cms where cms.user.id=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        query.setMaxResults(1000);
        List<ExamComment> as = query.getResultList();
        return as;
    }

    @Override
    public ExamComment findExamComment(String id) {
        return entityManager.find(ExamComment.class, id);

    }

    @Override
    public void updateExamComment(ExamComment comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from ExamComment cms";
        entityManager.createQuery(q).executeUpdate();
    }

    @Override
    public List<ExamComment> findExamCommentByExamination(String qid) {
        String q = "Select cms from ExamComment cms where cms.exam.id=:qid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("qid", qid);
        query.setMaxResults(1000);
        List<ExamComment> as = query.getResultList();
        return as;
    }

    @Override
    public void deleteCommentByExamination(String qid) {
        String q = "delete from ExamComment cms where cms.exam.id=:qid";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("qid", qid);
        qu.executeUpdate();
    }
    
    @Override
    public long getCommentNumByExamination(String qid) {
        String q = "Select count(ms) from ExamComment ms where ms.exam.id=:qid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", qid);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }
    
    
    
}
