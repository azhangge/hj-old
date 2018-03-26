package com.hjedu.common.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.common.dao.LogoDAO;
import com.hjedu.platform.entity.AdvModel;
import com.hjedu.platform.entity.Logo;

public class LogoDAOImpl extends BaseDAOImpl<Logo> implements LogoDAO,Serializable {
	@PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
	
	@Override
    public List<Logo> findAllLogoByBusinessId(String businessId) {
		List<Logo> ys;
    	String q;
    	q = "Select ass from Logo ass where ass.businessId = :businessId";
    	Query query = entityManager.createQuery(q);
    	query.setParameter("businessId", businessId);
    	
    	ys = query.getResultList();
        if(ys.size()==0){
        	Logo a = new Logo();
        	ys.add(a);
        }
        return ys;
    }
	@Override
    public Logo findLogo(String id){
		Logo a = this.entityManager.find(Logo.class, id);
        return a;
    }
}
