package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.platform.dao.ICasusDAO;
import com.hjedu.platform.entity.CasusModel;

public class CasusDAO implements ICasusDAO ,Serializable{

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void updateCasus(CasusModel ca) {
        this.entityManager.merge(ca);
    }

    @Override
    public CasusModel findCasus(String id) {
        return this.entityManager.find(CasusModel.class, id);
    }

    @Override
    public List<CasusModel> findAllCasuses() {
        String q = "Select cs from CasusModel cs order by cs.ord, cs.inputdate desc";
        List<CasusModel> ais = this.entityManager.createQuery(q).getResultList();
        return ais;
    }
    
    @Override
    public List<CasusModel> findAllCasusesByBusinessId(String businessId) {
        String q = "Select cs from CasusModel cs where cs.businessId=:businessId  order by cs.ord, cs.inputdate desc";
        List<CasusModel> ais = this.entityManager.createQuery(q).setParameter("businessId", businessId).getResultList();
        return ais;
    }
    
    @Override
    public List<CasusModel> findCasusesByAdmin(String adminid) {
    	String q;
    	List<CasusModel> ais;
    	if(adminid.equals("0")){
    		q = "Select cs from CasusModel cs where cs.admin.id= 10000000000000000 order by cs.ord, cs.inputdate desc";
            ais = this.entityManager.createQuery(q).getResultList();
    	}else{
            q = "Select cs from CasusModel cs where cs.admin.id=:adminid order by cs.ord, cs.inputdate desc";
            ais = this.entityManager.createQuery(q).setParameter("adminid", adminid).getResultList();
    	}
        return ais;
    }

    @Override
    public void deleteCasus(String id) {
        CasusModel p = this.entityManager.find(CasusModel.class, id);
        this.entityManager.remove(p);
    }

    @Override
    public void addCasus(CasusModel ca) {
        this.entityManager.persist(ca);
    }
    
    @Override
    public void updateCasusCountPlusOne(String paramString) {
        CasusModel yc = this.getEntityManager().find(CasusModel.class, paramString);
        yc.setCount(yc.getCount() + 1);
        this.getEntityManager().merge(yc);
    }
}
