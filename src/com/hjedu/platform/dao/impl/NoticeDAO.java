package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.entity.CourseType;
import com.hjedu.platform.dao.INoticeDAO;
import com.hjedu.platform.entity.NoticeModel;

public class NoticeDAO implements INoticeDAO ,Serializable{

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void updateNotice(NoticeModel nm) {
        this.entityManager.merge(nm);
    }

    @Override
    public NoticeModel findNotice(String id) {
        return this.entityManager.find(NoticeModel.class, id);
    }

    @Override
    public List<NoticeModel> findAllNotices() {
        String q = "Select nm from NoticeModel nm order by nm.ord, nm.createDate desc";
        List<NoticeModel> ais = this.entityManager.createQuery(q).getResultList();
        return ais;
    }
    
    @Override
    public List<NoticeModel> findNoticesByAdmin(String adminid) {
    	String q;
    	List<NoticeModel> ais;
    	if(adminid.equals("0")){
    		q = "Select nm from NoticeModel nm where nm.admin.id= 10000000000000000 order by nm.ord, nm.createDate desc";
            ais = this.entityManager.createQuery(q).getResultList();
    	}else{
            q = "Select nm from NoticeModel nm where nm.admin.id=:adminid order by nm.ord, nm.createDate desc";
            ais = this.entityManager.createQuery(q).setParameter("adminid", adminid).getResultList();
    	}
        return ais;
    }
    @Override
    public List<NoticeModel> findAllNoticesByBusinessId(String businessId) {
    	String q;
    	List<NoticeModel> ais;
        q = "Select nm from NoticeModel nm where nm.businessId=:businessId order by nm.ord, nm.createDate desc";
        ais = this.entityManager.createQuery(q).setParameter("businessId", businessId).getResultList();
        return ais;
    }

    @Override
    public void deleteNotice(String id) {
    	NoticeModel p = this.entityManager.find(NoticeModel.class, id);
        this.entityManager.remove(p);
    }

    @Override
    public void addNotice(NoticeModel nm) {
        this.entityManager.persist(nm);
    }
    
    @Override
    public void updateNoticeCountPlusOne(String paramString) {
    	NoticeModel yc = this.getEntityManager().find(NoticeModel.class, paramString);
        yc.setCount(yc.getCount() + 1);
        this.getEntityManager().merge(yc);
    }
    
    public List<NoticeModel> findNoticeByIds(List<String> idList){
    	String q = "Select nm from NoticeModel nm where nm.id in :ids";
        Query qu = this.entityManager.createQuery(q).setParameter("ids", idList);
        return qu.getResultList();
    }
}
