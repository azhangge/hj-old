package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.entity.PictureModel;

public interface IPictureDAO {

	public abstract void addPictureModel(PictureModel p);

	public abstract PictureModel findPictureModel(String pid);

	public abstract void updatePictureModel(PictureModel p);

	List<PictureModel> findAllPictureModelByBusinessId(String businessId);

	public abstract List<PictureModel> findPictureModelByUsr(String uid);

	public abstract void deletePictureModel(String pid);

	public abstract List<PictureModel> findPictureModelByadmin();

	List<PictureModel> findPictureModelByadmin(AdminInfo ai);
}
