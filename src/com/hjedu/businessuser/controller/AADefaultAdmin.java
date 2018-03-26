package com.hjedu.businessuser.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.hjedu.businessuser.dao.IBusinessUserDao;
import com.hjedu.businessuser.entity.BusinessUser;
import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamModule2DAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.impl.ExamModule2Service;
import com.hjedu.examination.service.impl.ExaminationService;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.hjedu.platform.entity.AuthorityWraper;
import com.hjedu.platform.service.ILogService;
import com.huajie.app.util.Validator;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;
import com.hjedu.customer.controller.AdminSessionBean;

@ManagedBean
@ViewScoped
public class AADefaultAdmin implements Serializable {
	BbsUser user = new BbsUser();
    IBbsUserDAO bbsUserDAO = SpringHelper.getSpringBean("BbsUserDAO");
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
    ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
    AdminInfo admin = new AdminInfo();
    boolean flag = false;
    Map mapTemp = new HashMap();
    List<AuthorityWraper> authList = new LinkedList();
    TreeNode moduleRoot = new DefaultTreeNode();
    TreeNode selectedModules[];
    List<ExamModuleModel> selectedModules2 = new LinkedList();
    List<Examination> exams = new LinkedList();
    List<LessonType> lessonTypes = new LinkedList();
    String urnTip = "";
    boolean urnOk = true;
    String emailTip = "";
    boolean emailOk = true;
    String phoneTip = "";
    boolean phoneOk = true;
    String businessId;
    
    public AdminInfo getAdmin() {
        return admin;
    }
    
    public void setAdmin(AdminInfo admin) {
        this.admin = admin;
    }
    
    public List<Examination> getExams() {
        return exams;
    }
    
    public void setExams(List<Examination> exams) {
        this.exams = exams;
    }
    
    public List<LessonType> getLessonTypes() {
        return lessonTypes;
    }
    
    public void setLessonTypes(List<LessonType> lessonTypes) {
        this.lessonTypes = lessonTypes;
    }
    
    public TreeNode[] getSelectedModules() {
        return selectedModules;
    }
    
    public void setSelectedModules(TreeNode[] selectedModules) {
        this.selectedModules = selectedModules;
    }
    
    public List<ExamModuleModel> getSelectedModules2() {
        return selectedModules2;
    }
    
    public void setSelectedModules2(List<ExamModuleModel> selectedModules2) {
        this.selectedModules2 = selectedModules2;
    }
    
    public TreeNode getModuleRoot() {
        return moduleRoot;
    }
    
    public void setModuleRoot(TreeNode moduleRoot) {
        this.moduleRoot = moduleRoot;
    }
    
    public Map getMapTemp() {
        return this.mapTemp;
    }
    
    public void setMapTemp(Map mapTemp) {
        this.mapTemp = mapTemp;
    }
    
    public boolean isFlag() {
        return flag;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    public List<AuthorityWraper> getAuthList() {
        return authList;
    }
    
    public void setAuthList(List<AuthorityWraper> authList) {
        this.authList = authList;
    }
 
    public String getUrnTip() {
		return urnTip;
	}

	public void setUrnTip(String urnTip) {
		this.urnTip = urnTip;
	}

	public boolean isUrnOk() {
		return urnOk;
	}

	public void setUrnOk(boolean urnOk) {
		this.urnOk = urnOk;
	}

	public String getEmailTip() {
		return emailTip;
	}

	public void setEmailTip(String emailTip) {
		this.emailTip = emailTip;
	}

	public boolean isEmailOk() {
		return emailOk;
	}

	public void setEmailOk(boolean emailOk) {
		this.emailOk = emailOk;
	}
	
	public String getPhoneTip() {
		return phoneTip;
	}

	public void setPhoneTip(String phoneTip) {
		this.phoneTip = phoneTip;
	}

	public boolean isPhoneOk() {
		return phoneOk;
	}

	public void setPhoneOk(boolean phoneOk) {
		this.phoneOk = phoneOk;
	}

	@PostConstruct
    public void init() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String id = req.getParameter("id");
        
        /* 判断是否为企业客户生成默认操作员 */
        String businessId = req.getParameter("businessId");
        if(StringUtils.isNotBlank(businessId)){
        	this.businessId = businessId;
        	IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");
        	BusinessUser businessUser = businessUserDao.findBussinessUser(businessId);
        	if(businessUser.getHasAdmin()){
        		id = businessUser.getAdminId();
        	}
        } 
        
        if (id != null) {
            this.admin = this.adminDAO.findAdmin(id);
            this.flag = true;
        }       
    }
    
    public String onChangeUrn() {
//      String urn = this.user.getUsername().trim(); 
      String urn = this.admin.getUrn().trim();
      
      if (urn != null && !"".equals(urn)) {
    	  AdminInfo ai=this.adminDAO.findAdminByUrnByBusinessId(urn,this.businessId);	
          if (ai != null) {
              this.urnOk = false;
              this.urnTip = "此用户名已存在";
          } else {
              this.urnOk = true;
              this.urnTip = "此用户名可以注册";
          }
      } else{
      	this.urnOk = false;
          this.urnTip = "用户名不能为空";
      }
      return null;
  }
    
    public String onChangeEmail() {
        String email = this.admin.getEmail();
        if (email != null && !"".equals(email)) {
            if (!email.contains("@")) {
                this.emailOk = false;
                this.emailTip = "邮箱格式不正确";
                return null;
            }
            AdminInfo ai = this.adminDAO.findAdminByEmail(email);
            BbsUser bu = this.bbsUserDAO.findBbsUserByEmail(email);
            if (ai != null && bu != null) {
                this.emailOk = false;
                this.emailTip = "此邮箱已存在";
            } else {
                this.emailOk = true;
                this.emailTip = "此邮箱可以注册";
            }
        }
        return null;
    }
    
    public String onChangePhone() {
    	String phone = this.admin.getTel().trim();
    	String truePhone;
    	if (phone != null && !"".equals(phone)) {
    		if(Validator.isMobile(phone)){//是合格的手机号
				truePhone=Validator.trueMobile(phone);//处理后的真实号码
				AdminInfo ai = this.adminDAO.findAdminByPhoneByBusinessId(truePhone,this.businessId);
	            if (ai != null) {
	                this.phoneOk = false;
	                this.phoneTip = "此手机号已存在";
	            } else {
	                this.phoneOk = true;
	                this.phoneTip = "此手机号可以注册";
	            }
    		}else{
    			this.phoneOk = false;
                this.phoneTip = "请填写正确的手机号";
    		}
        } else{
        	this.phoneOk = false;
            this.phoneTip = "手机号不能为空";
        }
    	return null;
    }
   
    public String submit_action() {

        this.admin.setBusinessId(this.businessId);
        this.admin.setGrp("user");
        this.admin.setChecked(true);
        FacesContext fc = FacesContext.getCurrentInstance();
        AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
        this.admin.setSuperId(asb.getAdmin().getId());
        
        if (!flag) {
            AdminInfo temp = this.adminDAO.findAdminByUrnByBusinessId(admin.getUrn(),this.businessId);
            if (temp != null) {
                FacesMessage fm = new FacesMessage();
                fm.setSeverity(FacesMessage.SEVERITY_ERROR);
                fm.setSummary("此用户名已存在！");
                fc.addMessage("", fm);
                return null;
            }
            this.admin.setId(this.businessId);// 迁移数据。特殊处理
            this.admin.setLtime(new Date());
            this.admin.setGenTime(new Date());
            this.admin.setAuthstr("lesson;examination;export;module;sys;score;class;analysis;safety;market;question;notice;user;talk;room;");
            adminDAO.addAdmin(admin);
            logger.log("添加了管理员：" + this.admin.getNickname() + ",ID:" + this.admin.getId());
            
        	IBusinessUserDao businessUserDao = SpringHelper.getSpringBean("BusinessUserDAO");
        	BusinessUser businessUser = businessUserDao.findBussinessUser(this.admin.getBusinessId());
        	if(businessUser!=null){
        		if(!businessUser.getHasAdmin()) {
        			businessUser.setHasAdmin(true);
        			businessUser.setAdminId(this.admin.getId());
        			businessUserDao.updateBusinessUser(businessUser);
        			logger.log("添加了默认管理员：" + this.admin.getNickname() + ",ID:" + this.admin.getId());
        		}
        	}
            
        }  else {
            AdminInfo c = asb.getAdmin();
            adminDAO.updateAdmin(admin);
            logger.log("修改了管理员：" + admin.getNickname());
            if (c.getId().equals(admin.getId())) {
                asb.setAdmin(admin);
            }       
        }
        asb.refreshAdmin();  
        return "ListBusinessUser?faces-redirect=true";
    }
    
}
