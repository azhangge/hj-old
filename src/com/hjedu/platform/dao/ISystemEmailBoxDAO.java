package com.hjedu.platform.dao;


import java.util.List;

import com.hjedu.platform.entity.SystemEmailBoxModel;

public abstract interface ISystemEmailBoxDAO
{
  public abstract void synEmailBox(List<SystemEmailBoxModel> paramList);

  public abstract List<SystemEmailBoxModel> findAllEmailBox();
}
