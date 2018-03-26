package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.SystemConfig;

public class SystemConfigDAO implements ISystemConfigDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public SystemConfig getSystemConfigByBusinessId(String businessId) {
        String q = "Select ais from SystemConfig ais where ais.businessId = :businessId";
        SystemConfig ais = null;
        try {
        	Query query = entityManager.createQuery(q);
        	query.setParameter("businessId", businessId);
            List<SystemConfig> aiss = query.getResultList();
            if (!aiss.isEmpty()) {
                ais = aiss.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ais == null) {
            ais = new SystemConfig();
        }
        return ais;
    }

    @Override
    public void updateSystemConfig(SystemConfig n) {
        this.entityManager.merge(n);
    }

    @Override
    public void saveSystemConfig(SystemConfig n) {
        this.entityManager.persist(n);
    }
    
    @Override
    public void saveTheme(String theme) {
        SystemConfig ssc = this.getSystemConfig();
        ssc.setTheme(theme.trim());
        this.entityManager.merge(ssc);
    }

	@Override
	public SystemConfig getSystemConfig() {
		String q = "Select ais from SystemConfig ais";
        SystemConfig ais = null;
        try {
            List<SystemConfig> aiss = entityManager.createQuery(q).getResultList();
            if (!aiss.isEmpty()) {
                ais = aiss.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ais == null) {
            ais = new SystemConfig();
        }
        return ais;
	}
}
