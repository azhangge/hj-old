package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.entity.SystemInfo;

public class SystemInfoDAO implements ISystemInfoDAO, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void updateSystemInfo(SystemInfo m) {
        this.entityManager.merge(m);
    }
    
    @Override
    public SystemInfo findSystemInfoByBusinessId(String businessId) {
        String q = "Select cq from SystemInfo cq  where  cq.businessId= :businessId ";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        
        SystemInfo cqs = null;
        try {
            cqs = (SystemInfo) qu.getSingleResult();
        } catch (Exception e) {
        }
        if (cqs == null) {
            cqs = new SystemInfo();
        }
        return cqs;
    }
    
    
}
