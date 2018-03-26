package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.hazelcast.util.CollectionUtil;
import com.hjedu.common.dao.LogoDAO;
import com.hjedu.platform.dao.IAdvDAO;
import com.hjedu.platform.dao.ISystemInfoDAO;
import com.hjedu.platform.dao.impl.SystemInfoDAO;
import com.hjedu.platform.entity.AdvModel;
import com.hjedu.platform.entity.Logo;
import com.hjedu.platform.entity.SystemInfo;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class Adv  implements Serializable{

    IAdvDAO advDAO = SpringHelper.getSpringBean("AdvDAO");
    AdvModel advsMap;
    Logo logo;
    SystemInfo systemInfo;

    public SystemInfo getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}

	public AdvModel getAdvsMap() {
        return advsMap;
    }

    public void setAdvsMap(AdvModel advsMap) {
        this.advsMap = advsMap;
    }

    public Logo getLogo() {
		return logo;
	}

	public void setLogo(Logo logo) {
		this.logo = logo;
	}

	@PostConstruct
    public void init() {
		//获取企业ID
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
        this.advsMap = advDAO.findAdvByBusinessId(businessId).get(0);
        LogoDAO dao = SpringHelper.getSpringBean("LogoDAO");        
        String wheresql = " o.businessId =?1 ";
		Object[] queryParams = {businessId};
        Map<String, Object> map = dao.getScrollData(Logo.class, 0, 1, wheresql, queryParams, null);
        @SuppressWarnings("unchecked")
		List<Logo> logos = (List<Logo>) map.get("list");
        if(CollectionUtil.isNotEmpty(logos)){
        	logo = logos.get(0);
        }else{
        	logo = new Logo();
        }
        
        ISystemInfoDAO infoDAO = SpringHelper.getSpringBean("SystemInfoDAO");
        this.systemInfo = infoDAO.findSystemInfoByBusinessId(businessId);
    }
}