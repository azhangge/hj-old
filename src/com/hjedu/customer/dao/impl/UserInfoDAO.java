package com.hjedu.customer.dao.impl;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import com.hjedu.customer.dao.IUserInfoDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.UserInfo;


public class UserInfoDAO implements IUserInfoDAO, Serializable {
	private static final Logger logger = Logger.getLogger(UserInfoDAO.class);
    @PersistenceContext
    private EntityManager entityManager;

	public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

	@Override
	public void addUserInfo(UserInfo userInfo) {
		this.entityManager.persist(userInfo);
	}

	@Override
	public void updateUserInfo(UserInfo userInfo) {
		this.entityManager.merge(userInfo);	
	}

	@Override
	public void deleteUserInfo(String id) {
		UserInfo userInfo = this.entityManager.find(UserInfo.class, id);
		this.entityManager.remove(userInfo);
	}

	@Override
	public UserInfo findUserInfo(String id) {
		UserInfo userInfo = this.entityManager.find(UserInfo.class, id);
		return userInfo;
	}
}
