package com.ucenter.json;

import java.util.List;

public class ImportUserListJson extends BaseJson{
	List<UserDetail> userDetailist;

	public List<UserDetail> getUserDetailist() {
		return userDetailist;
	}

	public void setUserDetailist(List<UserDetail> userDetailist) {
		this.userDetailist = userDetailist;
	}
}
