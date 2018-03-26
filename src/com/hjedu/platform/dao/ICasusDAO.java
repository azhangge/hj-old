package com.hjedu.platform.dao;


import java.util.List;

import com.hjedu.platform.entity.CasusModel;

public abstract interface ICasusDAO
{
  public abstract CasusModel findCasus(String id);

  public abstract List<CasusModel> findAllCasuses();
  public abstract List<CasusModel> findAllCasusesByBusinessId(String businessId);

  public abstract void deleteCasus(String id);

  public abstract void addCasus(CasusModel model);

  public abstract void updateCasus(CasusModel model);
  
  public void updateCasusCountPlusOne(String id);

List<CasusModel> findCasusesByAdmin(String adminid);

}