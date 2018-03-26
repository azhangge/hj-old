package com.ucenter.json;

import java.util.List;

public class ChangePasswordJson extends BaseJson{
	private String token;
	private String user_id;
	private List<UserSys> userSyslist;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<UserSys> getUserSyslist() {
		return userSyslist;
	}

	public void setUserSyslist(List<UserSys> userSyslist) {
		this.userSyslist = userSyslist;
	}
	
}
