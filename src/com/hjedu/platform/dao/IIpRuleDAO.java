package com.hjedu.platform.dao;


import java.util.List;

import com.hjedu.platform.entity.IpRule;

public abstract interface IIpRuleDAO
{
  public abstract IpRule findIpRule(String paramString);

  public abstract List<IpRule> findAllIpRule();
  
  public abstract List<IpRule> findAllBlackIpRule();
  
  public abstract List<IpRule> findAllWhiteIpRule();

  public abstract void deleteIpRule(String paramString);

  public abstract void addIpRule(IpRule paramIpRule);

  public abstract void updateIpRule(IpRule paramIpRule);

}