package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IQuestionCollectionDAO;
import com.hjedu.examination.entity.QuestionCollection;
import com.huajie.util.SpringHelper;

public class QuestionCollectionDAO implements IQuestionCollectionDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<QuestionCollection> findAllQuestionCollection() {
        System.out.println("begain");
        String q = "Select cq from QuestionCollection cq  order by cq.genTime desc";
        List<QuestionCollection> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<QuestionCollection> findQuestionCollectionByUser(String userId, String qtype) {
        String q = "Select cq from QuestionCollection cq where cq.bbsUser.id=:uid and cq.qtype=:qtype order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", userId);
        qu.setParameter("qtype", qtype);
        List<QuestionCollection> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public long getQuestionCollectionNumByUsrAndQtype(String userId,String qtype) {
        String q = "Select count(ms) from QuestionCollection ms where ms.bbsUser.id=:uid and ms.qtype=:qtype";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", userId);
        qu.setParameter("qtype", qtype);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public List<QuestionCollection> findQuestionCollectionByUser(String userId) {
        String q = "Select cq from QuestionCollection cq where cq.bbsUser.id=:uid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", userId);
        List<QuestionCollection> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public QuestionCollection findQuestionCollectionByQandU(String qid, String uid) {
        String q = "Select cq from QuestionCollection cq where cq.qid=:qid and cq.bbsUser.id=:uid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", qid);
        qu.setParameter("uid", uid);
        List<QuestionCollection> cqs = qu.getResultList();
        if (cqs != null) {
            if (!cqs.isEmpty()) {
                return cqs.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public QuestionCollection findQuestionCollection(String id) {
        return this.entityManager.find(QuestionCollection.class, id);
    }

    @Override
    public void addQuestionCollection(QuestionCollection log) {
        this.entityManager.persist(log);
    }

    @Override
    public void deleteQuestionCollection(String id) {
        QuestionCollection log = this.entityManager.find(QuestionCollection.class, id);
        this.entityManager.remove(log);
    }

    @Override
    public void deleteAllQuestionCollectionByUser(String uid) {
        String q = "delete from QuestionCollection cq where cq.bbsUser.id=:uid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", uid);
        qu.executeUpdate();
    }

    @Override
    public void deleteAllQuestionCollectionByUser(String uid, String qtype) {
        String q = "delete from QuestionCollection cq where cq.bbsUser.id=:uid and cq.qtype=:qtype";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("uid", uid);
        qu.setParameter("qtype", qtype);
        qu.executeUpdate();
    }

    public static void main(String args[]) {
        IQuestionCollectionDAO qDAO = SpringHelper.getSpringBean("QuestionCollectionDAO");
        qDAO.findAllQuestionCollection();

    }

}
