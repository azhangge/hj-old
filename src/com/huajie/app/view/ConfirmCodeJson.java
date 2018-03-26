package com.huajie.app.view;

public class ConfirmCodeJson extends BaseJson{
	private String token;
	private String user_id;
	private String targetSysId;
	
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
}
