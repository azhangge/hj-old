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

import com.hjedu.examination.dao.IQuestionCommentDAO;
import com.hjedu.examination.entity.QuestionComment;

public class QuestionCommentDAO implements IQuestionCommentDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addQuestionComment(QuestionComment partnerModel) {
        entityManager.persist(partnerModel);
    }

    @Override
    public void deleteQuestionComment(String id) {
        QuestionComment cm = entityManager.find(QuestionComment.class, id);
        entityManager.remove(cm);
    }

    @Override
    public List<QuestionComment> findAllQuestionComment() {
        String q = "Select cms from QuestionComment cms order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setMaxResults(1000);
        List<QuestionComment> as = query.getResultList();
        return as;
    }

    @Override
    public List<QuestionComment> findQuestionCommentByUsr(final String uid) {
        String q = "Select cms from QuestionComment cms where cms.user.id=:uid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("uid", uid);
        query.setMaxResults(1000);
        List<QuestionComment> as = query.getResultList();
        return as;
    }

    @Override
    public QuestionComment findQuestionComment(String id) {
        return entityManager.find(QuestionComment.class, id);

    }

    @Override
    public void updateQuestionComment(QuestionComment comModel) {
        entityManager.merge(comModel);
    }

    @Override
    public void deleteAll() {
        String q = "delete from QuestionComment cms";
        entityManager.createQuery(q).executeUpdate();
    }

    @Override
    public List<QuestionComment> findQuestionCommentByQuestion(String qid) {
        String q = "Select cms from QuestionComment cms where cms.questionId=:qid order by cms.genTime desc";
        Query query = entityManager.createQuery(q);
        query.setParameter("qid", qid);
        query.setMaxResults(1000);
        List<QuestionComment> as = query.getResultList();
        return as;
    }

    @Override
    public void deleteCommentByQuestion(String qid) {
        String q = "delete from QuestionComment cms where cms.questionId=:qid";
        Query qu=entityManager.createQuery(q);
        qu.setParameter("qid", qid);
        qu.executeUpdate();
    }
    
    @Override
    public long getCommentNumByQuestion(String qid) {
        String q = "Select count(ms) from QuestionComment ms where ms.questionId=:qid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", qid);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }
    
    
    
}
