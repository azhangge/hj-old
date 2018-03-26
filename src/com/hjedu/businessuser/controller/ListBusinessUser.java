package com.hjedu.businessuser.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListBusinessUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<BusinessUser> businessUserList = new ArrayList<BusinessUser>();
	
	ILogService logger=SpringHelper.getSpringBean("LogService");
	IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");

	AdminInfo user = ((AdminSessionBean) JsfHelper.getBean("adminSessionBean")).getAdmin();
	
	public AdminInfo getUser() {
		return user;
	}

	public void setUser(AdminInfo user) {
		this.user = user;
	}

	public List<BusinessUser> getBusinessUserList() {
		return businessUserList;
	}

	public void setBusinessUserList(List<BusinessUser> businessUserList) {
		this.businessUserList = businessUserList;
	}
	
	public ListBusinessUser(){
		this.businessUserList.clear();
		this.businessUserList = this.businessUserDao.findAllBusinessUser();
	}

	public String delete(String id) {
		BusinessUser businessUser = businessUserDao.findBussinessUser(id);
		businessUser.setDeleteFlag(1);
		businessUserDao.updateBusinessUser(businessUser);
		resumeDB();
		return "ListBusinessUser";
	}
	
	public void resumeDB(){
		this.businessUserList.clear();
		this.businessUserList = this.businessUserDao.findAllBusinessUser();
	}
	
	public void operBuisnessUser(String id) {
		BusinessUser businessUser = businessUserDao.findBussinessUser(id);
        if (businessUser.getDeleteFlag()==1) {
            this.logger.log("禁用了管理员：" + businessUser.getBusinessNameCn());
            businessUser.setDeleteFlag(0);
        } else {
        	businessUser.setDeleteFlag(1);
            this.logger.log("激活了管理员：" + businessUser.getBusinessNameCn());
        }
        businessUserDao.updateBusinessUser(businessUser);
        for (BusinessUser b : this.businessUserList) {
            if (b.getId().equals(id)) {
                if (b.getDeleteFlag()==1) {
                    b.setDeleteFlag(0);
                } else {
                    b.setDeleteFlag(1);
                }
                break;
            }
        }
    }
}
