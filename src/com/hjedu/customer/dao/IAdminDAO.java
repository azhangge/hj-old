package com.hjedu.customer.dao;



import java.util.List;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;

public abstract interface IAdminDAO
{
	public abstract void addAdmin(AdminInfo paramAdminInfo);

	public abstract void updateAdmin(AdminInfo paramAdminInfo);

	public abstract void deleteAdmin(String paramString);

	public abstract AdminInfo findAdmin(String paramString);

	public abstract AdminInfo findAdminByUrnByBusinessId(String urn, String businessId);
	
	public abstract AdminInfo findAdminByEmail(String email);
	
	public abstract AdminInfo findAdminByPhone(String tel);
	
	public abstract List<AdminInfo> findAllAdmin();

	public abstract List<AdminInfo> findAllCompanyAdmin();
	
	public abstract List<AdminInfo> findAllByBusinessId(String businessId);
	
	public abstract void setCarouselVersion(AdminInfo ai);

	List<AdminInfo> findAdminsByQuery(String q);

	public abstract List<AdminInfo> findAdminsBySuperId(String id);

	public AdminInfo findAdminByPhoneByBusinessId(String tel,String businessId);
}