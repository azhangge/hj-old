package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hazelcast.util.CollectionUtil;
import com.hjedu.common.dao.LogoDAO;
import com.hjedu.platform.dao.IAdvDAO;
import com.hjedu.platform.entity.AdvModel;
import com.hjedu.platform.entity.Logo;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListAdv implements Serializable {

    //ILogService logger = SpringHelper.getSpringBean("LogService");
    List<AdvModel> advList;
    List<Logo> advList2;
    IAdvDAO advDAO = SpringHelper.getSpringBean("AdvDAO");
    //HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    //HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

    String businessId;
    
    public String getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    
    public List<AdvModel> getAdvList() {
        return this.advList;
    }

    public void setAdvList(List<AdvModel> advList) {
        this.advList = advList;
    }

    public List<Logo> getAdvList2() {
		return advList2;
	}

	public void setAdvList2(List<Logo> advList2) {
		this.advList2 = advList2;
	}

	public ListAdv() {
        synchronizeDB();
    }

    @SuppressWarnings("unchecked")
	private void synchronizeDB() {
    	this.setBusinessId(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        this.advList = advDAO.findAdvByBusinessId(this.getBusinessId());
        LogoDAO dao = SpringHelper.getSpringBean("LogoDAO");
        advList2 = (List<Logo>) dao.findAllLogoByBusinessId(this.getBusinessId());
        if(CollectionUtil.isEmpty(advList2)){
        	Logo l = new Logo();
        	advList2.add(l);
        }
    }
}