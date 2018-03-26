package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.huajie.app.util.StringUtil;

public class AdminDAO implements IAdminDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addAdmin(AdminInfo paramAdminInfo) {
        this.entityManager.persist(paramAdminInfo);
    }

    @Override
    public void deleteAdmin(String paramString) {
        AdminInfo ai = this.entityManager.find(AdminInfo.class, paramString);
        this.entityManager.remove(ai);
    }

    @Override
    public AdminInfo findAdmin(String paramString) {
    	AdminInfo ai = null;
    	if(!StringUtil.isEmpty(paramString)){
    		ai = this.entityManager.find(AdminInfo.class, paramString);
    	}
        return ai;
    }

    @Override
    public AdminInfo findAdminByUrnByBusinessId(String urn, String businessId) {
        String q = "Select ais from AdminInfo ais where ais.urn=:urn and ais.businessId = :businessId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("urn", urn);
        qu.setParameter("businessId", businessId);
        List<AdminInfo> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            AdminInfo ai = (AdminInfo) as.get(0);
            return ai;
        }
    }
    
    @Override
    public List<AdminInfo> findAllAdmin() {
        String q = "Select ais from AdminInfo ais";
        List<AdminInfo> ais = entityManager.createQuery(q).getResultList();
        return ais;
    }

    @Override
    public void updateAdmin(AdminInfo paramAdminInfo) {
        this.entityManager.merge(paramAdminInfo);
    }

	@Override
	public void setCarouselVersion(AdminInfo ai) {
		ai.setCarouselVersion(ai.getCarouselVersion()+1);
		updateAdmin(ai);
	}
	
	@Override
	public List<AdminInfo> findAdminsByQuery(String q){
		List<AdminInfo> ais = entityManager.createQuery(q).getResultList();
		return ais;
	}
	
	@Override
	public List<AdminInfo> findAllByBusinessId(String businessId) {
		String q = "Select ais from AdminInfo ais where ais.businessId=:businessId ";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        List<AdminInfo> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            return as;
        }
	}
	
	@Override
	public List<AdminInfo> findAdminsBySuperId(String superId) {
		String q = "Select ais from AdminInfo ais where ais.superId=:superId or ais.id=:superId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("superId", superId);
        List<AdminInfo> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            return as;
        }
	}

	@Override
	public List<AdminInfo> findAllCompanyAdmin(){
		String q = "Select ais from AdminInfo ais where ais.grp='company'";
        List<AdminInfo> ais = entityManager.createQuery(q).getResultList();
        return ais;
	}
	
	
	@Override
	public AdminInfo findAdminByEmail(String email) {
		String q = "Select ais from AdminInfo ais where ais.email=:email";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("email", email);
        List<AdminInfo> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            AdminInfo ai = (AdminInfo) as.get(0);
            return ai;
        }
	}

	@Override
	public AdminInfo findAdminByPhone(String tel) {
		String q = "Select ais from AdminInfo ais where ais.tel=:tel";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("tel", tel);
        List<AdminInfo> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            AdminInfo ai = (AdminInfo) as.get(0);
            return ai;
        }
	}
	
	@Override
	public AdminInfo findAdminByPhoneByBusinessId(String tel,String businessId) {
		String q = "Select ais from AdminInfo ais where ais.tel=:tel and ais.businessId=:businessId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("tel", tel);
        qu.setParameter("businessId", businessId);
        List<AdminInfo> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            AdminInfo ai = (AdminInfo) as.get(0);
            return ai;
        }
	}
}
