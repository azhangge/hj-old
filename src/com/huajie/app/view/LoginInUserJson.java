package com.huajie.app.view;

import java.util.List;

import com.hjedu.businessuser.entity.BusinessUser;

public class LoginInUserJson extends BaseJson{
	private String token;
	private String targetSysId;
	private List<UserSys> userSyslist;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
