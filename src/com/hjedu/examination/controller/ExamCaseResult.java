
package com.hjedu.examination.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@RequestScoped
public class ExamCaseResult {

    ExamCase examCase = null;

    public ExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
    }

    @PostConstruct
    public void init() {
        //String caseId = JsfHelper.getRequest().getSession().getAttribute("tempExamCase").toString();
        String caseId = JsfHelper.getFlash().get("tempExamCase").toString();
        IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");
        examCase = examCaseService.retrieveExamCase(caseId);
    }

}
