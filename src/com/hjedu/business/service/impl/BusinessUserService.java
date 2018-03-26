package com.hjedu.business.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huajie.cache.IRereCacheInstance;
import com.huajie.cache.RereCacheInstanceReplicated;
import com.huajie.cache.RereCacheManager;
import com.huajie.util.JsfHelper;
import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;

public class BusinessUserService implements Serializable {

	private static IRereCacheInstance ins = null;
	
	private IBusinessUserDao businessUserDao;

	public IBusinessUserDao getBusinessUserDao() {
		return businessUserDao;
	}

	public void setBusinessUserDao(IBusinessUserDao businessUserDao) {
		this.businessUserDao = businessUserDao;
	}

	public IRereCacheInstance getInstance() {
        if (ins == null) {
            this.reBuildCache();
        }
        return ins;
    }
    
    public void reBuildCache() {
    	ins = RereCacheManager.getReplicatedInstance("businessIds");
    	RereCacheInstanceReplicated ci = (RereCacheInstanceReplicated) ins;
    	//1.清除所有缓存
        ci.removeAllWithoutMsg();
        
        List<BusinessUser> businessUsers = businessUserDao.findAllEffectiveBusinessUser();
        if(businessUsers==null) return;
        BusinessUser defaultBusinessUser = null;
        Map<String,BusinessUser> businessUserMap = new HashMap<String,BusinessUser>();
        for(BusinessUser businessUser : businessUsers){
        	businessUserMap.put(businessUser.getDomainName(), businessUser);
        	if("1".equals(businessUser.getIsDefault())){
        		defaultBusinessUser = businessUser;
        	}
        }
        if(defaultBusinessUser==null){
        	throw new RuntimeException("没有默认的企业用户");
        }
        ci.addWithoutMsg("defaultBusinessUser", defaultBusinessUser);
        ci.addWithoutMsg("businessUserMap", businessUserMap);
    }
    
    public BusinessUser findDefaultBusinessUser(){
    	
    	IRereCacheInstance<BusinessUser> ci = getInstance();
    	BusinessUser defaultBusinessUser = ci.fetchObject("defaultBusinessUser");
    	return defaultBusinessUser;
    }
    
    public BusinessUser findBusinessUserByDomain(String domain){
    	if(domain==null){
        	throw new RuntimeException("domain不能为null");
        }
    	IRereCacheInstance<Map<String,BusinessUser>> ci = getInstance();
    	Map<String,BusinessUser> businessUserMap = ci.fetchObject("businessUserMap");
    	if(businessUserMap!=null){
    		BusinessUser businessUser = businessUserMap.get(domain);
    		if(businessUser!=null) return businessUser;
    			
    		businessUser = businessUserDao.findBussinessUserByDomain(domain);
    		if(businessUser!=null) {
    			reBuildCache();
    			return businessUser;
    		}
    	}
    	return null;
    }
}
