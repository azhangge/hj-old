package com.hjedu.businessuser.dao;

import java.util.List;

import com.hjedu.businessuser.entity.BusinessUser;

public interface IBusinessUserDao{

	void addBusinessUser(BusinessUser businessUser);
	
	void updateBusinessUser(BusinessUser businessUser);
	
	void deleteBusinessUser(String paramString);
	
	public List<BusinessUser> findAllBusinessUser();
	
	public List<BusinessUser> findAllEffectiveBusinessUser();
	
	public BusinessUser findBussinessUser(String paramString);
	
	public BusinessUser findBussinessUserByDomain(String domain);
	
	public BusinessUser findDefaultBussinessUser();
	
	public BusinessUser findOpenBussinessUser();
	
	public List<BusinessUser> findAllOpenBussinessUser();
}
