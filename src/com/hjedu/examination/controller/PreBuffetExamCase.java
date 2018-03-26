package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IBuffetExaminationDAO;
import com.hjedu.examination.entity.buffet.BuffetExamination;
import com.hjedu.platform.controller.ClientSession;
import com.huajie.util.JsfHelper;
import com.huajie.util.MyLogger;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class PreBuffetExamCase implements Serializable {

    IBuffetExaminationDAO examDAO = SpringHelper.getSpringBean("BuffetExaminationDAO");
    //IUserSessionStateService iussService = SpringHelper.getSpringBean("UserSessionStateService");
    ClientSession cs = JsfHelper.getBean("clientSession");
    BuffetExamination exam;

    public BuffetExamination getExam() {
        return exam;
    }

    public void setExam(BuffetExamination exam) {
        this.exam = exam;
    }

    @PostConstruct
    public void init() {

        HttpServletRequest request = JsfHelper.getRequest();
        String examId = request.getParameter("exam_id");
        //String id = module_id;
        if (examId != null) {
            this.exam = this.examDAO.findExamination(examId);
            if (exam.getParts() != null) {
                Collections.sort(exam.getParts());
            }

        }

    }

    public String begainTest() {
        //HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            JsfHelper.getRequest().getSession().setAttribute("tempBuffetExam", this.exam);
            ExamCaseBuffet eb = JsfHelper.getBean("examCaseBuffet");
            if (eb == null) {
                eb = new ExamCaseBuffet();
                JsfHelper.getRequest().getSession().setAttribute("examCaseBuffet", eb);
            }
            eb.init();
            String url = "ExamCaseBuffet?faces-redirect=true";
            MyLogger.echo(url);
//            response.sendRedirect(url);
//            FacesContext.getCurrentInstance().responseComplete();
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        
    }

}
