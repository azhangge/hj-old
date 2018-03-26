package com.huajie.seller.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.huajie.seller.dao.ISaleOrderDAO;
import com.huajie.seller.model.SaleOrder;

public class SaleOrderDAO implements ISaleOrderDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<SaleOrder> findAllSaleOrder() {
        String q = "select yis from SaleOrder yis order by yis.genTime desc";
        List<SaleOrder> ps = entityManager.createQuery(q).getResultList();
        return ps;
    }

    @Override
    public List<SaleOrder> findAccessibleSaleOrder(String uid) {
        String q = "select yis from SaleOrder yis where yis.seller.highLevel like '%" + uid + "%' order by yis.genTime desc";
        List<SaleOrder> ps = entityManager.createQuery(q).getResultList();
        return ps;
    }

    @Override
    public List<SaleOrder> findSaleOrderByAgent(String uid) {
        String q = "select yis from SaleOrder yis where yis.agentId=:id order by yis.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", uid);
        List<SaleOrder> ps = qu.getResultList();
        return ps;
    }

    @Override
    public List<SaleOrder> findSaleOrderByUser(String uid) {
        String q = "select yis from SaleOrder yis where yis.user.id=:id order by yis.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", uid);
        List<SaleOrder> ps = qu.getResultList();
        return ps;
    }
    
    @Override
    public List<SaleOrder> findFinishedSaleOrderByUser(String uid) {
        String q = "select yis from SaleOrder yis where yis.user.id=:id  and yis.status=='processed' order by yis.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", uid);
        List<SaleOrder> ps = qu.getResultList();
        return ps;
    }
    
    @Override
    public List<SaleOrder> findUnFinishedSaleOrderByUser(String uid) {
        String q = "select yis from SaleOrder yis where yis.user.id=:id and yis.status!='processed' order by yis.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", uid);
        List<SaleOrder> ps = qu.getResultList();
        return ps;
    }

    @Override
    public List<SaleOrder> findSaleOrderBySeller(String uid) {
        String q = "select yis from SaleOrder yis where yis.seller.id=:id order by yis.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", uid);
        List<SaleOrder> ps = qu.getResultList();
        return ps;
    }

    @Override
    public SaleOrder findSaleOrderByOid(String oid) {
        String q = "select yis from SaleOrder yis where yis.oid=:id order by yis.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", oid);
        List<SaleOrder> ps = qu.getResultList();
        if (ps != null) {
            if (ps.isEmpty()) {
                return null;
            } else {
                return ps.get(0);
            }
        }
        return null;
    }

    @Override
    public List<SaleOrder> findSaleOrderByType(final String id) {
        String q = "select yis from SaleOrder yis where yis.typeId=:id order by yis.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<SaleOrder> ps = qu.getResultList();
        return ps;
    }

    @Override
    public SaleOrder findSaleOrder(String id) {
        SaleOrder p = (SaleOrder) this.entityManager.find(SaleOrder.class, id);
        return p;
    }

    @Override
    public void updateSaleOrder(SaleOrder partnerModel) {
        this.entityManager.merge(partnerModel);
    }

    @Override
    public void deleteSaleOrder(String id) {
        SaleOrder yp = (SaleOrder) this.entityManager.find(SaleOrder.class, id);
        this.entityManager.remove(yp);
    }

    @Override
    public void addSaleOrder(SaleOrder partnerModel) {
        this.entityManager.persist(partnerModel);
    }
}
