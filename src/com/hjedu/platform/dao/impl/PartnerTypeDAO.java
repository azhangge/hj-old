package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.IPartnerTypeDAO;
import com.hjedu.platform.entity.PartnerType;

public class PartnerTypeDAO implements IPartnerTypeDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addPartnerType(PartnerType paramPartnerType) {
        this.entityManager.persist(paramPartnerType);
    }

    @Override
    public void deletePartnerType(String paramString) {
        PartnerType yp = (PartnerType) this.entityManager.find(PartnerType.class, paramString);
        this.entityManager.remove(yp);
    }

    @Override
    public List<PartnerType> findAllPartnerType() {
        String q = "select yis from PartnerType yis order by yis.ord";
        List<PartnerType> ps = entityManager.createQuery(q).getResultList();
        return ps;
    }

    @Override
    public PartnerType findPartnerType(String paramString) {
        PartnerType p = (PartnerType) this.entityManager.find(PartnerType.class, paramString);
        return p;
    }

    @Override
    public void updatePartnerType(PartnerType paramPartnerType) {
        this.entityManager.merge(paramPartnerType);
    }
}
