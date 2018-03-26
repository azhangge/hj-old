package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.AdvModel;


public interface IAdvDAO {

    public abstract List<AdvModel> findAllAdv();

    public abstract void updateAdvModel(AdvModel paramAdvModel);

    public abstract AdvModel findAdv(String paramString);

	public abstract List<AdvModel> findAdvByAdminId();
	
	public abstract AdvModel findAdvByAdminId(String id);

	void addAdvModel(AdvModel am);

	List<AdvModel> findAdvByBusinessId(String businessId);

}
