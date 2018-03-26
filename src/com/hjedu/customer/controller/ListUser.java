package com.hjedu.customer.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;

import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.dao.IAdminDAO;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListUser  implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//ILogService logger = SpringHelper.getSpringBean("JsfLogService");
    IAdminDAO adminDAO = SpringHelper.getSpringBean("AdminDAO");
    //ICasusDAO casusDAO = SpringHelper.getSpringBean("CasusDAO");
    //IPictureDAO picDAO = SpringHelper.getSpringBean("PictureDAO");
    //IAdminFileDAO fileDAO = SpringHelper.getSpringBean("AdminFileDAO");
    //IOperationLogDAO logDAO=SpringHelper.getSpringBean("OperationLogDAO");
    ILogService logger=SpringHelper.getSpringBean("LogService");
    List<AdminInfo> userList = new ArrayList<AdminInfo>();
    AdminInfo user = ((AdminSessionBean) JsfHelper.getBean("adminSessionBean")).getAdmin();
    
    private String urn;
    private String nickname;
    private String persona;
    private SelectItem[] grp;

	public SelectItem[] getGrp() {
        return grp;
	}

	public void setGrp(SelectItem[] grp) {
		this.grp = grp;
	}
	
	public String getUrn() {
		return urn;
	}

	public void setUrn(String urn) {
		this.urn = urn;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPersona() {
		return persona;
	}

	public void setPersona(String persona) {
		this.persona = persona;
	}

	public AdminInfo getUser() {
        return this.user;
    }
    
    public void setUser(AdminInfo user) {
        this.user = user;
    }
    
    public List<AdminInfo> getUserList() {
        return this.userList;
    }
    
    public void setUserList(List<AdminInfo> userList) {
        this.userList = userList;
    }
    
    public ListUser() {
    	this.userList.clear();
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	this.userList = adminDAO.findAllByBusinessId(businessId);
        
        String[] s = {"超级管理员","管理员"};
		grp = new SelectItem[s.length + 1];
		grp[0] = new SelectItem("", "请选择");
        for (int i=0;i<s.length;i++) {
        	grp[i+1] = new SelectItem(s[i], s[i]);
        }
    }
    
    public String delete(String id) {
        //HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        //String id = request.getParameter("id");

        AdminInfo u = adminDAO.findAdmin(id);
        if (this.user.getUrn().equals(u.getUrn())) {
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage fm = new FacesMessage();
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            fm.setSummary("超级管理员不能删除自身！");
            fc.addMessage("", fm);
            return null;
        }
        AdminInfo at = null;
        List<AdminInfo> as=this.adminDAO.findAllAdmin();
        for (AdminInfo ai : as) {//获得一个不是正在被删除并且是超级管理员的ID，此功能将被删除的管理员发布的信息的UID改为获得的非被删的超级管理员
            if (ai.getGrp().equals("admin") && ai.getId() != id) {
                at = ai;
                break;
            }
        }
        MyLogger.echo(at.getId()+":"+at.getNickname());

        
        logger.log("删除了管理员：" + u.getNickname() + ",ID:" + id);
        adminDAO.deleteAdmin(id);
        
        resumeDB();
        
        return "ListUser";
    }
    
    void resumeDB() {
        this.userList = adminDAO.findAllAdmin();
    }
    
    public void getUsersByQuery(){
    	String q = "Select ais from AdminInfo ais where 1=1";
    	if(StringUtils.isNotEmpty((this.urn))){
    		q += " and ais.urn like '%"+urn+"%'";
    	}
    	if(StringUtils.isNotEmpty((this.nickname))){
    		q += " and ais.nickname like '%"+nickname+"%'";
    	}
    	if(StringUtils.isNotEmpty((this.persona))){
    		q += " and ais.persona like '%"+persona+"%'";
    	}
    	this.userList = adminDAO.findAdminsByQuery(q);
    }
    
    public void someAbleUser(String id) {
    	AdminInfo ai = adminDAO.findAdmin(id);
        if (ai.getEnabled()) {
            this.logger.log("禁用了管理员：" + ai.getUrn());
            ai.setEnabled(false);
        } else {
        	ai.setEnabled(true);
            this.logger.log("激活了管理员：" + ai.getUrn());
        }
        adminDAO.updateAdmin(ai);
        for (AdminInfo b : this.userList) {
            if (b.getId().equals(id)) {
                if (b.getEnabled()) {
                    b.setEnabled(false);
                } else {
                    b.setEnabled(true);
                }
                break;
            }
        }
    }
    
    public void someCheckUser(String id) {
    	AdminInfo ai = adminDAO.findAdmin(id);
        if (ai.getChecked()) {
            this.logger.log("取消审核了用户：" + ai.getUrn());
            ai.setChecked(false);
        } else {
        	ai.setChecked(true);
            this.logger.log("审核了用户：" + ai.getUrn());
        }
        adminDAO.updateAdmin(ai);
        for (AdminInfo b : this.userList) {
            if (b.getId().equals(id)) {
                if (b.getChecked()) {
                    b.setChecked(false);
                } else {
                    b.setChecked(true);
                }
                break;
            }
        }
    }
}
