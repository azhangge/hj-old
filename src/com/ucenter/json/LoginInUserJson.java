package com.ucenter.json;

import java.util.List;

public class LoginInUserJson extends BaseJson{
	private String token;
	private String user_id;
	private String targetSysId;
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
	public String getTargetSysId() {
		return targetSysId;
	}
	public void setTargetSysId(String targetSysId) {
		this.targetSysId = targetSysId;
	}
	public List<UserSys> getUserSyslist() {
		return userSyslist;
	}
	public void setUserSyslist(List<UserSys> userSyslist) {
		this.userSyslist = userSyslist;
	}

}
