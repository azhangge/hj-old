package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.PartnerModel;

public interface IPartnerDAO {

    public abstract void addPartnerModel(PartnerModel paramPartnerModel);

    public abstract PartnerModel findPartnerModel(String paramString);

    public abstract void updatePartnerModel(PartnerModel paramPartnerModel);

    public abstract List<PartnerModel> findAllPartnerModel();

    public abstract List<PartnerModel> findPartnerModelByType(String id);

    public abstract void deletePartnerModel(String paramString);
}
