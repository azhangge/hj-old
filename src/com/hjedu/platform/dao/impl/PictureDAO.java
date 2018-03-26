package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.dao.IPictureDAO;
import com.hjedu.platform.entity.PictureModel;
import com.huajie.util.ExternalUserUtil;

@SuppressWarnings("serial")
public class PictureDAO implements IPictureDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void addPictureModel(PictureModel p) {
        this.getEntityManager().persist(p);
    }

    public void deletePictureModel(String pid) {
        PictureModel p = this.getEntityManager().find(PictureModel.class, pid);
        this.getEntityManager().remove(p);
    }

    @SuppressWarnings("unchecked")
	public List<PictureModel> findAllPictureModelByBusinessId(String businessId) {
//    	AdminInfo ai = ExternalUserUtil.getAdminByUser();
//    	List<PictureModel> ys;
//    	String q;
//    	if(ai.getId()=="0"){
//    		q = "Select ass from PictureModel ass where ass.adminId = 10000000000000000 order by ass.ord, ass.genTime desc";
//    		ys = this.entityManager.createQuery(q).getResultList();
//    	}else{
//    		q= "Select ass from PictureModel ass where ass.adminId = :adminId order by ass.ord, ass.genTime desc";
//    		ys = this.entityManager.createQuery(q).setParameter("adminId", ai.getId()).getResultList();
//    	}
//    	return ys;
    	
    	String q = "Select ass from PictureModel ass where ass.businessId = :businessId order by ass.ord, ass.genTime desc";
    	Query query = this.entityManager.createQuery(q);
    	query.setParameter("businessId", businessId);
    	List<PictureModel> ys = query.getResultList();
    	return ys;
    }

    @Override
    public List<PictureModel> findPictureModelByUsr(final String uid) {
        String q = "Select ass from PictureModel ass where ass.editor.id=:id order by ass.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", uid);
        List<PictureModel> ys = qu.getResultList();
        return ys;
    }

    public PictureModel findPictureModel(String pid) {
        return this.getEntityManager().find(PictureModel.class, pid);
    }

    public void updatePictureModel(PictureModel p) {
        this.getEntityManager().merge(p);
    }

	@Override
	public List<PictureModel> findPictureModelByadmin() {
//    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
//    	List<PictureModel> ys;
//    	String q;
//    	if(ai.getId()=="0"){
//    		q = "Select ass from PictureModel ass where ass.adminId = 10000000000000000 order by ass.ord, ass.genTime desc";
//            ys = this.entityManager.createQuery(q).getResultList();
//    	}else{
//    		q = "Select ass from PictureModel ass where ass.adminId = :adminId order by ass.ord, ass.genTime desc";
//            ys = this.entityManager.createQuery(q).setParameter("adminId", ai.getId()).getResultList();
//    	}
//        return ys;
		String q = "Select ass from PictureModel ass order by ass.ord, ass.genTime desc";
    	List<PictureModel> ys = this.entityManager.createQuery(q).getResultList();
    	return ys;
    }
	
	@Override
	public List<PictureModel> findPictureModelByadmin(AdminInfo ai) {
        String q = "Select ass from PictureModel ass where ass.adminId = :adminId order by ass.ord, ass.genTime desc";
        List<PictureModel> ys = this.entityManager.createQuery(q).setParameter("adminId", ai.getId()).getResultList();
        return ys;
    }
}
