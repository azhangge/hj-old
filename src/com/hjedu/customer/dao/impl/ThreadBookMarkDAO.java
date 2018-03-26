package com.hjedu.customer.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IThreadBookMarkDAO;
import com.hjedu.platform.entity.BbsThreadBookMark;
import com.hjedu.platform.entity.BbsThreadTrade;

public class ThreadBookMarkDAO implements IThreadBookMarkDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<BbsThreadBookMark> findThreadBookMarkByThread(String tid) {
        String q = "Select tts from BbsThreadBookMark tts where tts.thread.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", tid);
        List<BbsThreadBookMark> ys = qu.getResultList();
        return ys;
    }

    @Override
    public List<BbsThreadBookMark> findThreadBookMarkByUsr(String tid) {
        String q = "Select tts from BbsThreadBookMark tts where tts.user.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", tid);
        List<BbsThreadBookMark> ys = qu.getResultList();
        return ys;
    }

    @Override
    public void updateThreadBookMark(BbsThreadBookMark tt) {
        this.entityManager.merge(tt);
    }

    @Override
    public BbsThreadBookMark findThreadBookMark(String id) {
        return this.entityManager.find(BbsThreadBookMark.class, id);
    }

    @Override
    public void addThreadBookMark(BbsThreadBookMark trade) {
        this.entityManager.persist(trade);
    }

    @Override
    public void deleteThreadBookMark(String id) {
        BbsThreadBookMark b = this.entityManager.find(BbsThreadBookMark.class, id);
        this.entityManager.remove(b);
    }
}
