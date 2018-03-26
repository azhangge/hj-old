package com.hjedu.platform.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.common.dao.impl.BaseDAOImpl;
import com.hjedu.platform.dao.INoticeUserDAO;
import com.hjedu.platform.entity.NoticeUser;

public class NoticeUserDAO extends BaseDAOImpl<NoticeUser> implements INoticeUserDAO ,Serializable{

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public NoticeUser findNoticeUser(String id) {
        return this.entityManager.find(NoticeUser.class, id);
    }
    
    @Override
    public List<NoticeUser> findAllNoticeUser() {
        String q = "Select nm from NoticeUser nm";
        List<NoticeUser> ais = this.entityManager.createQuery(q).getResultList();
        return ais;
    }
    
    @Override
    public void deleteNoticeUser(String id) {
    	NoticeUser p = this.entityManager.find(NoticeUser.class, id);
        this.entityManager.remove(p);
    }
    
    @Override
    public void addNoticeUser(NoticeUser nm) {
        this.entityManager.persist(nm);
    }
    
    @Override
    public void updateNoticeUser(NoticeUser nm) {
        this.entityManager.merge(nm);
    }

	@Override
	public List<NoticeUser> findByUserId(String userid,int firstindex,int maxresult,Map<String,String> orderby) {
		String wheresql = " o.userId =?1 ";
		Object[] queryParams = {userid};
		return this.getScrollData(firstindex, maxresult, wheresql, queryParams , orderby);
	}

	@Override
	public List<NoticeUser> findReadNoticeListByUser(String userid,int firstindex,int maxresult,Map<String,String> orderby){
		String wheresql = " o.userId =?1 and o.isReaded =1 ";
		Object[] queryParams = {userid};
		return this.getScrollData(firstindex, maxresult, wheresql, queryParams , orderby);
	}

	@Override
	public List<NoticeUser> findUnreadNoticeListByUser(String userid, int firstindex, int maxresult,Map<String, String> orderby) {
		String wheresql = " o.userId =?1 and o.isReaded =0 ";
		Object[] queryParams = {userid};
		return this.getScrollData(firstindex, maxresult, wheresql, queryParams , orderby);
	}

}
