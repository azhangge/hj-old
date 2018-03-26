package com.hjedu.platform.dao;

import com.hjedu.platform.entity.CustomerService;



public abstract interface ICustomerServiceDAO
{
  public abstract CustomerService getCustomerService();

  public abstract void updateCustomerService(CustomerService sc);
  
  
}
