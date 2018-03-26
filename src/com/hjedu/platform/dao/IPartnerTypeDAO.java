package com.hjedu.platform.dao;

import java.util.List;

import com.hjedu.platform.entity.PartnerType;


public interface IPartnerTypeDAO {
    
  public abstract void addPartnerType(PartnerType paramPartnerType);

  public abstract PartnerType findPartnerType(String paramString);

  public abstract void updatePartnerType(PartnerType paramPartnerType);

  public abstract List<PartnerType> findAllPartnerType();

  public abstract void deletePartnerType(String paramString);
}
