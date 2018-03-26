package com.hjedu.platform.dao;

import com.hjedu.platform.entity.SystemConfig;



public abstract interface ISystemConfigDAO
{
	
  SystemConfig getSystemConfig();
  
  SystemConfig getSystemConfigByBusinessId(String businessId);

  public abstract void updateSystemConfig(SystemConfig sc);
  
  public abstract void saveSystemConfig(SystemConfig sc);
  
  public void saveTheme(String theme);
  
}
