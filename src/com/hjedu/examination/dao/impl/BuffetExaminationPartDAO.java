package com.hjedu.examination.dao.impl;


import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IBuffetExaminationPartDAO;
import com.hjedu.examination.entity.buffet.BuffetExaminationPart;

public class BuffetExaminationPartDAO implements IBuffetExaminationPartDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addBuffetExaminationPart(BuffetExaminationPart m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteBuffetExaminationPart(String id) {
        BuffetExaminationPart c = this.entityManager.find(BuffetExaminationPart.class, id);
        if (c != null) {
            this.entityManager.remove(c);
        }
    }

    @Override
    public List<BuffetExaminationPart> findAllBuffetExaminationPart() {
        String q = "Select cq from BuffetExaminationPart cq order by cq.genTime desc";
        List<BuffetExaminationPart> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<BuffetExaminationPart> findAllBuffetExaminationPartByPaper(String pid) {
        String q = "Select cq from BuffetExaminationPart cq where cq.paper.id=:pid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("pid", pid);
        List<BuffetExaminationPart> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public BuffetExaminationPart findBuffetExaminationPart(String id) {
        BuffetExaminationPart c = this.entityManager.find(BuffetExaminationPart.class, id);
        return c;
    }

    @Override
    public void updateBuffetExaminationPart(BuffetExaminationPart m) {
        this.entityManager.merge(m);
    }
}
