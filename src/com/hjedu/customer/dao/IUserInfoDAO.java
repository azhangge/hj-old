package com.hjedu.customer.dao;

import com.hjedu.customer.entity.UserInfo;

public interface IUserInfoDAO {
	public abstract void addUserInfo(UserInfo userInfo);

    public abstract void updateUserInfo(UserInfo userInfo);

    public abstract void deleteUserInfo(String id);

    public abstract UserInfo findUserInfo(String id);
}