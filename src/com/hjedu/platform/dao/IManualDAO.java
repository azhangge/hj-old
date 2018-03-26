package com.hjedu.platform.dao;


import java.util.List;

import com.hjedu.platform.entity.ManualModel;

public abstract interface IManualDAO
{
  public abstract ManualModel findManual(String paramString);

  public abstract List<ManualModel> findAllManual();

  public abstract void deleteManual(String paramString);

  public abstract void addManual(ManualModel paramManualModel);

  public abstract void updateManual(ManualModel paramManualModel);

}