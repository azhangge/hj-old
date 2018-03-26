package com.hjedu.businessuser.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.huajie.app.util.StringUtil;

public class BusinessUserDaoImpl implements IBusinessUserDao, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

	@Override
	public void addBusinessUser(BusinessUser businessUser) {
		 this.entityManager.persist(businessUser);
	}

	@Override
	public void updateBusinessUser(BusinessUser businessUser) {
		this.entityManager.merge(businessUser);

	}

	@Override
	public void deleteBusinessUser(String paramString) {
		this.entityManager.remove(paramString);
	}

	@Override
	public List<BusinessUser> findAllBusinessUser() {
		String q = "Select bu from BusinessUser bu";
		List<BusinessUser> bu = entityManager.createQuery(q).getResultList();
		return bu;
	}

	@Override
	public BusinessUser findBussinessUser(String paramString) {
		BusinessUser bu = null;
		if(!StringUtil.isEmpty(paramString)){
			bu = this.entityManager.find(BusinessUser.class, paramString);
    	}
        return bu;
	}

	@Override
	public BusinessUser findBussinessUserByDomain(String domain) {
		String q = "Select bu from BusinessUser bu where bu.domainName=:domainName";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("domainName", domain);
        List<BusinessUser> bus = qu.getResultList();
        if (bus.isEmpty()) {
            return null;
        } else {
        	BusinessUser bu = (BusinessUser) bus.get(0);
            return bu;
        }
	}

	public BusinessUser findOpenBussinessUser() {
		String q = "Select bu from BusinessUser bu where bu.isOpen = true";
		List<BusinessUser> bu = entityManager.createQuery(q).getResultList();
		if(!bu.isEmpty()){
			return bu.get(0);
		}
		return null;
	}

	@Override
	public BusinessUser findDefaultBussinessUser() {
		String q = "Select bu from BusinessUser bu where bu.isDefault= 1";
        Query qu = this.entityManager.createQuery(q);
        List<BusinessUser> bus = qu.getResultList();
        if (bus.isEmpty()) {
            return null;
        } else {
        	BusinessUser bu = (BusinessUser) bus.get(0);
            return bu;
        }
	}

	@Override
	public List<BusinessUser> findAllEffectiveBusinessUser() {
		String q = "Select bu from BusinessUser bu where bu.deleteFlag = 0";
		List<BusinessUser> bu = entityManager.createQuery(q).getResultList();
		return bu;
	}

	@Override
	public List<BusinessUser> findAllOpenBussinessUser() {
		String q = "Select bu from BusinessUser bu where bu.isOpen = true";
		List<BusinessUser> bu = entityManager.createQuery(q).getResultList();
		return bu;
	}
	
}
