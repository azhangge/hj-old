package com.hjedu.platform.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.course.dao.ICourseTypeDAO;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.SystemConfig;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.JsfHelper;
import com.huajie.util.JsonUtil;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class HJGeneral implements Serializable {

	private static final long serialVersionUID = 1L;
	
	IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");
	
	private BusinessUser businessUser;
	
	public BusinessUser getBusinessUser() {
		return businessUser;
	}

	public void setBusinessUser(BusinessUser businessUser) {
		this.businessUser = businessUser;
	}
	
    @PostConstruct
    public void init() {
    	//获取企业ID
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	
    	this.businessUser = businessUserDao.findBussinessUser(businessId);
    }
}
