package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.IPartnerDAO;
import com.hjedu.platform.entity.PartnerModel;

public class PartnerDAO implements IPartnerDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<PartnerModel> findAllPartnerModel() {
        String q = "select yis from PartnerModel yis order by yis.ord";
        List<PartnerModel> ps = entityManager.createQuery(q).getResultList();
        return ps;
    }

    @Override
    public List<PartnerModel> findPartnerModelByType(final String id) {
        String q = "select yis from PartnerModel yis where yis.typeId=:id order by yis.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<PartnerModel> ps = qu.getResultList();
        return ps;
    }

    @Override
    public PartnerModel findPartnerModel(String id) {
        PartnerModel p = (PartnerModel) this.entityManager.find(PartnerModel.class, id);
        return p;
    }

    @Override
    public void updatePartnerModel(PartnerModel partnerModel) {
        this.entityManager.merge(partnerModel);
    }

    @Override
    public void deletePartnerModel(String id) {
        PartnerModel yp = (PartnerModel) this.entityManager.find(PartnerModel.class, id);
        this.entityManager.remove(yp);
    }

    @Override
    public void addPartnerModel(PartnerModel partnerModel) {
        this.entityManager.persist(partnerModel);
    }
}
