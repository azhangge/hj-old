package com.hjedu.examination.dao.impl;


import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IModuleRandom2PartDAO;
import com.hjedu.examination.dao.IRandom2PaperPartDAO;
import com.hjedu.examination.entity.random2.Random2PaperPart;

public class Random2PaperPartDAO implements IRandom2PaperPartDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    

    @Override
    public void addRandom2PaperPart(Random2PaperPart m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteRandom2PaperPart(String id) {
        Random2PaperPart c = this.entityManager.find(Random2PaperPart.class, id);
        if (c != null) {
            
            this.entityManager.remove(c);
        }
    }

    @Override
    public List<Random2PaperPart> findAllRandom2PaperPart() {
        String q = "Select cq from Random2PaperPart cq order by cq.genTime desc";
        List<Random2PaperPart> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<Random2PaperPart> findAllRandom2PaperPartByPaper(String pid) {
        String q = "Select cq from Random2PaperPart cq where cq.paper.id=:pid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("pid", pid);
        List<Random2PaperPart> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public Random2PaperPart findRandom2PaperPart(String id) {
        Random2PaperPart c = this.entityManager.find(Random2PaperPart.class, id);
        return c;
    }

    @Override
    public void updateRandom2PaperPart(Random2PaperPart m) {
        this.entityManager.merge(m);
    }
}
