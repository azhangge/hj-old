package com.huajie.seller.dao.impl;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.huajie.seller.dao.ISaleGoodsDAO;
import com.huajie.seller.model.SaleGoods;
import com.huajie.util.RereRandom;

public class SaleGoodsDAO implements ISaleGoodsDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addSaleGoods(SaleGoods m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteSaleGoods(String id) {
        SaleGoods c = this.entityManager.find(SaleGoods.class, id);
        if (c != null) {
            this.entityManager.remove(c);
        }
    }

    @Override
    public List<SaleGoods> findAllSaleGoods() {
        String q = "Select cq from SaleGoods cq order by cq.genTime desc";
        List<SaleGoods> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public SaleGoods findSaleGoods(String id) {
        SaleGoods c = this.entityManager.find(SaleGoods.class, id);
        return c;
    }

    @Override
    public List<SaleGoods> findSaleGoodsByModule(String id) {
        String q = "Select cq from SaleGoods cq where cq.module.id=:id order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<SaleGoods> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<SaleGoods> findSaleGoodsByModule(String id, int offSet, int num) {
        String q = "Select cq from SaleGoods cq where cq.module.id=:id order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<SaleGoods> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<SaleGoods> findOrderedSaleGoodsByModule(String id, int offSet, int num,String field,String type) {
        String q = "Select cq from SaleGoods cq where cq.module.id=:id order by cq."+field+" "+type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<SaleGoods> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<SaleGoods> findSaleGoodsByModuleAndLike(String id,  Map<String, Object> fms) {
        String q1 = "Select cq from SaleGoods cq where cq.module.id=:id";
        String q2 = " order by cq.ord desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {

            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }

        Query qu = this.entityManager.createQuery(q1 + q2);
        qu.setParameter("id", id);
        List<SaleGoods> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<SaleGoods> findGoodsByLike(String str) {
        String q1 = "Select cq from SaleGoods cq where cq.name like :str";
        Query qu = this.entityManager.createQuery(q1);
        qu.setParameter("str", "%"+str+"%");
        List<SaleGoods> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateSaleGoods(SaleGoods m) {
        this.entityManager.merge(m);
    }

    @Override
    public long getGoodsNumByModule(String id) {
        String q = "Select count(ms) from SaleGoods ms where ms.module.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public SaleGoods findGoodsByIndex(int index, String mid) {
        String q = "Select cq from SaleGoods cq where cq.module.id=:id order by cq.id desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", mid);
        qu.setFirstResult(index);
        qu.setMaxResults(1);
        SaleGoods cqs = (SaleGoods) qu.getResultList().get(0);
        return cqs;
    }
    
    @Override
    public SaleGoods findGoodsByGid(String mid) {
        String q = "Select cq from SaleGoods cq where cq.gid=:id order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", mid);
        qu.setMaxResults(1);
        SaleGoods cqs = (SaleGoods) qu.getResultList().get(0);
        return cqs;
    }
    
    @Override
    public SaleGoods findGoodsBySaleCode(String mid) {
        String q = "Select cq from SaleGoods cq where cq.saleCode=:id order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", mid);
        qu.setMaxResults(1);
        SaleGoods cqs = (SaleGoods) qu.getResultList().get(0);
        return cqs;
    }

    @Override
    public SaleGoods findSaleGoodsByName(String id) {
        String q = "Select cq from SaleGoods cq where cq.name=:name order by cq.ord desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<SaleGoods> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }
    
    @Override
    public SaleGoods findSaleGoodsByHashCode(String code) {
        String q = "Select cq from SaleGoods cq where cq.hashCode=:code order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("code", code);
        List<SaleGoods> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    @Override
    public List<SaleGoods> getRandomGoodsOfNumInModule(long num, String mid, List<SaleGoods> cqs2) {
        List<SaleGoods> cqs = new ArrayList();
        if (num == 0) {
            return cqs;
        }
        long total = this.getGoodsNumByModule(mid);
        long n = num > total ? total : num;
        if (total == n) {
            List temp = this.findSaleGoodsByModule(mid);
            return temp;
        }
        Set<Long> set = new HashSet();
        while (true) {
            long tem = (long) (total * Math.random());
            if (!set.contains(tem)) {
                set.add(tem);
                SaleGoods cq = this.findGoodsByIndex((int) tem, mid);
                if (cqs2.contains(cq)) {
                    total--;
                    n = num > total ? total : num;
                } else {
                    cqs.add(cq);
                }
            }
            if (cqs.size() >= n) {
                break;
            }

        }

        return cqs;
    }

    @Override
    public List<SaleGoods> getRandomGoodsOfNumInModule(long num, String mid) {
        List<SaleGoods> cqs = new ArrayList();
        if (num == 0) {
            return cqs;
        }
        long total = this.getGoodsNumByModule(mid);
        long n = num > total ? total : num;
        if (total == n) {
            List temp = this.findSaleGoodsByModule(mid);
            Collections.shuffle(temp);
            return temp;
        }
//        Set<Long> set = new HashSet();
//        while (set.size() < n) {
//            long tem = (long) (total * Math.random());
//            set.add(tem);
//        }
        Set<Long> set = RereRandom.randomSet(0, total, num);
        for (long l : set) {
            SaleGoods cq = this.findGoodsByIndex((int) l, mid);
            cqs.add(cq);
        }
        return cqs;
    }

    @Override
    public void deleteSaleGoodsByModule(String moduleId) {
        List<SaleGoods> jqs = this.findSaleGoodsByModule(moduleId);
        for (SaleGoods j : jqs) {
            this.deleteSaleGoods(j.getId());
        }
    }
}
