package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IBbsMessageDAO;
import com.hjedu.platform.entity.BbsMessage;

public class BbsMessageDAOImpl implements IBbsMessageDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addMessageModel(BbsMessage ca) {
        this.entityManager.persist(ca);
    }

    public void deleteMessageModel(String id) {
        BbsMessage p = this.entityManager.find(BbsMessage.class, id);
        this.entityManager.remove(p);
    }

    public List<BbsMessage> findAllMessageModel() {
        String q = "Select ms from BbsMessage ms order by ms.genTime desc";
        List<BbsMessage> ais = entityManager.createQuery(q).getResultList();
        return ais;
    }

    public BbsMessage findMessageModel(final String id) {
        BbsMessage c = this.entityManager.find(BbsMessage.class, id);
        return c;
    }

    public List<BbsMessage> findMessageModelByReceiver(final String id) {
        String q = "Select ms from BbsMessage ms where ms.receiver.id=:id and ms.receiverMarkDel=false order by ms.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<BbsMessage> ais = qu.getResultList();
        return ais;
    }

    @Override
    public List<BbsMessage> findUnReadedMsgByReceiver(final String id) {
        String q = "Select ms from BbsMessage ms where ms.receiver.id=:id and ms.receiverMarkDel=false and ms.readed=false order by ms.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<BbsMessage> ais = qu.getResultList();
        return ais;
    }
    
    @Override
    public void deleteMsgByReceiver(final String id) {
        String q = "delete from BbsMessage ms where ms.receiver.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.executeUpdate();
    }
    
    @Override
    public void deleteMsgBySender(final String id) {
        String q = "delete from BbsMessage ms where ms.sender.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.executeUpdate();
    }

    @Override
    public long getUnReadedMsgNumByReceiver(String id) {
        String q = "Select count(ms) from BbsMessage ms where ms.receiver.id=:id and ms.receiverMarkDel=false and ms.readed=false";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        String s=qu.getResultList().get(0).toString();
        long num=Long.parseLong(s);
        return num;
    }

    public List<BbsMessage> findMessageModelBySender(final String id) {
        String q = "Select ms from BbsMessage ms where ms.sender.id=:id and ms.senderMarkDel=false order by ms.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<BbsMessage> ais = qu.getResultList();
        return ais;
    }

    public void updateMessageModel(BbsMessage ca) {
        this.entityManager.merge(ca);
    }

    public void updateMessageReaded(String id) {
        BbsMessage m = this.entityManager.find(BbsMessage.class, id);
        m.setReaded(true);
        this.entityManager.merge(m);
    }

    @Override
    public void receiverMarkDel(String id) {
        BbsMessage m = this.entityManager.find(BbsMessage.class, id);
        m.setReceiverMarkDel(true);
        this.entityManager.merge(m);
    }

    @Override
    public void senderMarkDel(String id) {
        BbsMessage m = this.entityManager.find(BbsMessage.class, id);
        m.setSenderMarkDel(true);
        this.entityManager.merge(m);
    }
}
