package com.hjedu.customer.dao;

import java.util.List;

import com.hjedu.customer.entity.BbsUser;
import com.huajie.app.model.UserToken;

public abstract interface IUserTokenDAO {
	public void addUserToken(UserToken userToken);
	public void updateUserToken(UserToken userToken);
	public String getIdByToken(String token);
	public UserToken getByToken(String token);
	public void deleteUserTokenByToken(String token);
	void deleteUserToken(String paramString);
	List<UserToken> getUserTokensByToken(String token);
	void deleteUserTokensByToken(String token);
	public UserToken addUserTokenById(String id);
}
