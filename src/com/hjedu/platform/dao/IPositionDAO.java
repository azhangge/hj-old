package com.hjedu.platform.dao;


import java.util.List;

import com.hjedu.platform.entity.BbsPosition;

public abstract interface IPositionDAO
{
  public abstract BbsPosition findBbsPosition(String paramString);

  public abstract List<BbsPosition> findAllBbsPosition();
  
  public abstract void deleteBbsPosition(String paramString);

  public abstract void addBbsPosition(BbsPosition paramBbsPosition);

  public abstract void updateBbsPosition(BbsPosition paramBbsPosition);

}