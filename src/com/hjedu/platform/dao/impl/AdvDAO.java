package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.dao.IAdvDAO;
import com.hjedu.platform.entity.AdvModel;
import com.huajie.util.ExternalUserUtil;

public class AdvDAO implements IAdvDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<AdvModel> findAllAdv() {
        String q = "Select ass from AdvModel ass";
        List<AdvModel> ys = entityManager.createQuery(q).getResultList();
        return ys;
    }    
    
    @Override
    public List<AdvModel> findAdvByAdminId() {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q = "Select ass from AdvModel ass where ass.adminId = :adminId";
        List<AdvModel> ys = entityManager.createQuery(q).setParameter("adminId", ai.getId()).getResultList();
        if(ys.size()==0){
        	AdvModel a = new AdvModel();
        	ys.add(a);
        }
        return ys;
    }
    
    @Override
    public AdvModel findAdvByAdminId(String adminId) {
        String q = "Select ass from AdvModel ass where ass.adminId = :adminId";
        List<AdvModel> ys = entityManager.createQuery(q).setParameter("adminId", adminId).getResultList();
        if(ys.size()==0){
        	AdvModel a = new AdvModel();
        	ys.add(a);
        }
        return ys.get(0);
    }
    
    @Override
    public List<AdvModel> findAdvByBusinessId(String businessId) {
//    	AdminInfo ai = ExternalUserUtil.getAdminByUser();
    	List<AdvModel> ys;
    	String q;
    	q = "Select ass from AdvModel ass where ass.businessId = :businessId";
    	Query query = entityManager.createQuery(q);
    	query.setParameter("businessId", businessId);
    	
    	ys = query.getResultList();
        if(ys.size()==0){
        	AdvModel a = new AdvModel();
        	ys.add(a);
        }
        return ys;
    }
    
    @Override
    public void updateAdvModel(AdvModel am) {
        this.entityManager.merge(am);
    }
    
    @Override
    public void addAdvModel(AdvModel am) {
        this.entityManager.persist(am);
    }

    @Override
    public AdvModel findAdv(String id) {
        AdvModel a = this.entityManager.find(AdvModel.class, id);
        return a;
    }
}
