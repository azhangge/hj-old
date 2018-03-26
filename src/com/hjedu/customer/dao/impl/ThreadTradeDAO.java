package com.hjedu.customer.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IThreadTradeDAO;
import com.hjedu.platform.entity.BbsThreadTrade;


public class ThreadTradeDAO implements IThreadTradeDAO{
    
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<BbsThreadTrade> findThreadTradeByThread(String tid) {
        String q = "Select tts from BbsThreadTrade tts where tts.thread.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", tid);
        List<BbsThreadTrade> ys = qu.getResultList();
        return ys;
    }
    
    @Override
    public List<BbsThreadTrade> findThreadTradeByUsr(String tid) {
        String q = "Select tts from BbsThreadTrade tts where tts.user.id=:tid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("tid", tid);
        List<BbsThreadTrade> ys = qu.getResultList();
        return ys;
    }


    @Override
    public void updateThreadTrade(BbsThreadTrade tt) {
        this.entityManager.merge(tt);
    }

    @Override
    public BbsThreadTrade findThreadTrade(String id) {
        return this.entityManager.find(BbsThreadTrade.class, id);
    }

    @Override
    public void addThreadTrade(BbsThreadTrade trade) {
        this.entityManager.persist(trade);
    }
    
    @Override
    public void deleteThreadTrade(String id) {
        BbsThreadTrade b=this.entityManager.find(BbsThreadTrade.class, id);
        this.entityManager.remove(b);
    }
    
    
}
