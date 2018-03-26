package com.hjedu.platform.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.json.JSONObject;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.dao.IIntensiveExamAndUserDAO;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.IntensiveExamAndUser;
import com.hjedu.platform.dao.ISystemConfigDAO;
import com.huajie.app.util.StringUtil;
import com.huajie.corss.model.SubUser;
import com.huajie.corss.util.Conn;
import com.huajie.corss.util.ObjectConvent;
import com.huajie.util.IdcardValidator;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class CompleteInfo implements Serializable {
    private IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");    
	private IIntensiveExamAndUserDAO intensiveExamAndUserDAO = SpringHelper.getSpringBean("IntensiveExamAndUserDAO");
	private IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
	private ISystemConfigDAO sysDAO = SpringHelper.getSpringBean("SystemConfigDAO");
	String constants_sub_id=this.sysDAO.getSystemConfig().getSub_id();
    String yun_host=this.sysDAO.getSystemConfig().getYun_host();
	
	BbsUser usr = null;
	Examination exam = null;
	
	public Examination getExam() {
		return exam;
	}

	public void setExam(Examination exam) {
		this.exam = exam;
	}

	public BbsUser getUsr() {
        return usr;
    }
    
    public void setUsr(BbsUser usr) {
        this.usr = usr;
    }
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ClientSession cs = JsfHelper.getBean("clientSession");
        this.usr = cs.getUsr();
        if (this.usr != null) {
            String id = request.getParameter("id");
            this.exam=examDAO.findExamination(id);
        }
    }
    
    public void finish() {
    	if(StringUtil.isEmpty(this.usr.getName())){
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "真实姓名不能为空"));
    		return;
    	}
    	if(StringUtil.isEmpty(this.usr.getPid())){
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "身份证号不能为空"));
    		return;
    	}
    	if(StringUtil.isNotEmpty(this.usr.getPid())){
    		IdcardValidator iv = new IdcardValidator();
        	if(!iv.isValidatedAllIdcard(this.usr.getPid())){
        		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "不是有效的身份证号"));
        		return;
        	}
    	}
    	if(StringUtil.isEmpty(this.usr.getCompany())){
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "", "所属单位不能为空"));
    		return;
    	}
 
		SubUser subUser=ObjectConvent.BbsUser2SubUser(this.usr);
        JSONObject JSONObjectsubUser=new JSONObject(subUser);
        JSONObject jsonUpdate=Conn.updateSubUser(yun_host,JSONObjectsubUser);
        String statusUpdate=(String) jsonUpdate.get("status");
        if(statusUpdate.equals("1")){//修改成功
        	userDAO.updateBbsUser(this.usr);
        	IntensiveExamAndUser iea=intensiveExamAndUserDAO.getIntensiveExamAndUserByUserExamId(this.usr.getId(), this.exam.getId());
            if(iea==null){
            	IntensiveExamAndUser temp = new IntensiveExamAndUser();
            	temp.setExam(this.exam);
            	temp.setUser(this.usr);
            	temp.setIfAllow(false);
            	intensiveExamAndUserDAO.addIntensiveExamAndUser(temp);
            }
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "报名已成功"));
        }
    }
    
}
