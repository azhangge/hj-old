package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.dao.IIntensiveExamAndUserDAO;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.IntensiveExamAndUser;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class RegIntensiveExam implements Serializable {
    private IBbsUserDAO userDAO = SpringHelper.getSpringBean("BbsUserDAO");    
	private IIntensiveExamAndUserDAO intensiveExamAndUserDAO = SpringHelper.getSpringBean("IntensiveExamAndUserDAO");
	private IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
    
	BbsUser usr = null;
	Examination exam = null;
	
	boolean isReg=false;
	
	public boolean isReg() {
		return isReg;
	}

	public void setReg(boolean isReg) {
		this.isReg = isReg;
	}

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
            exam=examDAO.findExamination(id);
            IntensiveExamAndUser iea=intensiveExamAndUserDAO.getIntensiveExamAndUserByUserExamId(this.usr.getId(), id);
            if(iea!=null){
            	this.isReg=true;
            }
        }
    }
    
}
