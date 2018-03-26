package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.customer.controller.AdminSessionBean;
import com.hjedu.examination.dao.IBuffetExaminationDAO;
import com.hjedu.examination.entity.buffet.BuffetExamination;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListBuffetExamination implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IBuffetExaminationDAO examDAO = SpringHelper.getSpringBean("BuffetExaminationDAO");
    //IExamModule2DAO moduleDAO = SpringHelper.getSpringBean("ExamModule2DAO");
    List<BuffetExamination> exams;
    //ExamModuleModel module;

    public List<BuffetExamination> getExams() {
        return exams;
    }

    public void setExams(List<BuffetExamination> exams) {
        this.exams = exams;
    }


    @PostConstruct
    public void init() {
    	AdminSessionBean asb = JsfHelper.getBean("adminSessionBean");
    	String businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
    	if(asb.isIfSuper()){
    		this.exams = this.examDAO.findAllExamination(businessId);
    	}else{
    		this.exams = this.examDAO.findAllExaminationByAdmin(businessId);
    	}
        //Collections.sort(exams);
        //Collections.reverse(exams);
    }

    public void delete(String id) {
        BuffetExamination ee = this.examDAO.findExamination(id);
        this.logger.log("删除了考试：" + ee.getName());
        this.examDAO.deleteExamination(id);
        this.exams = this.examDAO.findAllExamination(CookieUtils.getBusinessId(JsfHelper.getRequest()));
        //Collections.sort(exams);
        //Collections.reverse(exams);
    }
    
    
    public String editOrd(String id) {
        for (BuffetExamination cq : this.exams) {
            if (id.equals(cq.getId())) {
                this.examDAO.updateExamination(cq);
                break;
            }
        }
        return null;
    }
}
