package com.huajie.util;

import java.util.List;

import com.hjedu.customer.entity.AdminInfo;

public interface ObjectWithAdmin {
	List<AdminInfo> getAdmins();

	void setAdmins(List<AdminInfo> admins);
}
