package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ExamScoreReport implements Serializable {

    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");
    ExamCase examCase = null;
    boolean ifSuSupplementary=false;

    public boolean isIfSuSupplementary() {
        return ifSuSupplementary;
    }

    public void setIfSuSupplementary(boolean ifSuSupplementary) {
        this.ifSuSupplementary = ifSuSupplementary;
    }
    


    public ExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String case_id = request.getParameter("case_id");
        if (case_id != null) {
            this.examCase = this.examCaseDAO.findExamCase(case_id);
        }
        if(this.examCase!=null){
             this.ifSuSupplementary=this.examCaseService.checkIfSupplementaryExamination(this.examCase.getExamination().getId(), this.examCase.getUser().getId());
        }
    }
}
