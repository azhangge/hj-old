package com.hjedu.businessuser.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.huajie.app.util.StringUtil;
import com.huajie.util.InitDB;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.service.ILogService;

@ManagedBean
@ViewScoped
public class AABusinessUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");

	private BusinessUser businessUser = new BusinessUser();
	
	ILogService logger = SpringHelper.getSpringBean("LogService");
	
	private boolean flag = false;//是否新增
	
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public BusinessUser getBusinessUser() {
		return businessUser;
	}

	public void setBusinessUser(BusinessUser businessUser) {
		this.businessUser = businessUser;
	}

	/**
	 * 初始化修改时使用
	 */
	@PostConstruct
    public void init() {
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        if(StringUtil.isEmpty(id)){
        	return;
        }
        this.flag = true;
        this.businessUser = businessUserDao.findBussinessUser(id);
	}
	
	/**
	 * 提交
	 * @return
	 */
	public String submit_action() {
		FacesContext fc = FacesContext.getCurrentInstance();
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        String grp = asb.getAdmin().getGrp();
        if(!"admin".equals(grp)){
        	 FacesMessage fm = new FacesMessage();
             fm.setSeverity(FacesMessage.SEVERITY_ERROR);
             fm.setSummary("只能超级管理员可以操作");
             fc.addMessage("", fm);
             return null;
        }
        logger.log("当前操作员： ID:"+asb.getAdmin().getId());
        if (!flag) {
        	this.businessUser.setCreateTime(new Date());
        	this.businessUser.setLastModifyTime(new Date());
        	this.businessUserDao.addBusinessUser(businessUser);
        	InitDB.initExamModule(this.businessUser.getId(), businessUser.getBusinessNameCn());
        	InitDB.initCourseType(this.businessUser.getId(), businessUser.getBusinessNameCn());
        	InitDB.initExamDepartment(this.businessUser.getId(), businessUser.getBusinessNameCn());
        	logger.log("添加了企业：" + this.businessUser.getBusinessNameCn() + ",ID:" + this.businessUser.getId());
        } else {
        	this.businessUser.setLastModifyTime(new Date());
        	this.businessUserDao.updateBusinessUser(businessUser);
        	logger.log("修改了企业：" + this.businessUser.getBusinessNameCn() + ",ID:" + this.businessUser.getId());
        }
        return "ListBusinessUser?faces-redirect=true";
	}
}
