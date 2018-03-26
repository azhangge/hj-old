package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.dao.IUserTokenDAO;
import com.huajie.app.model.UserToken;
import com.huajie.app.util.StringUtil;

public class UserTokenDAO implements IUserTokenDAO, Serializable {
	
	@PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

	@Override
	public void addUserToken(UserToken userToken) {
		this.entityManager.persist(userToken);
	}

	@Override
	public void updateUserToken(UserToken userToken) {
		this.entityManager.merge(userToken);
	}
    
	@Override
	public String getIdByToken(String token) {
		String id = null;
		List<UserToken> as = getUserTokensByToken(token);
		if(as!=null&&as.size()>0){
			id = as.get(0).getUser_id();
		}
		return id;
	}

	@Override
	public UserToken getByToken(String token) {
		String sql="select ut from UserToken ut where ut.token=:token";
		Query qu = this.entityManager.createQuery(sql);
		qu.setParameter("token", token);
		List<UserToken> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
        	UserToken ai = (UserToken) as.get(0);
            return ai;
        }
	}
	
	@Override
	public List<UserToken> getUserTokensByToken(String token) {
		if(!StringUtil.isEmpty(token)){
			String sql ="select ut from UserToken ut where ut.id=(select ut2.id from UserToken ut2 where ut2.token=:token)";
			Query qu = this.entityManager.createQuery(sql);
			@SuppressWarnings("unchecked")
			List<UserToken> as = qu.setParameter("token", token).getResultList();
        	return as;
		}
		return null;
	}
	
	@Override
    public void deleteUserToken(String paramString) {
		UserToken ai = this.entityManager.find(UserToken.class, paramString);
        this.entityManager.remove(ai);
    }
	
	@Override
    public void deleteUserTokensByToken(String token) {
		List<UserToken> uts = getUserTokensByToken(token);
		for(UserToken ut : uts){
			this.entityManager.remove(ut);
		}
    }
	
	@Override
    public void deleteUserTokenByToken(String token) {
		UserToken userToken=getByToken(token);
		this.entityManager.remove(userToken);
	}
	
	@Override
	public UserToken addUserTokenById(String id) {
		UserToken ut = new UserToken();
		ut.setId(id);
		ut.setCreateTime(new Date());
		addUserToken(ut);
		return ut;
	}

	
	
}
