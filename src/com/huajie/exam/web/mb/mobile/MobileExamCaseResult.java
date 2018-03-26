
package com.huajie.exam.web.mb.mobile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;

import com.hjedu.examination.entity.ExamCase;
import com.huajie.util.JsfHelper;

@ManagedBean
@RequestScoped
public class MobileExamCaseResult {

    ExamCase examCase = null;

    public ExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
    }

    @PostConstruct
    public void init() {
        examCase = (ExamCase) JsfHelper.getRequest().getSession().getAttribute("tempExamCase");
        JsfHelper.getRequest().getSession().removeAttribute("tempExamCase");
        JsfHelper.getRequest().getSession().removeAttribute("examCaseStep");
        
        //JsfHelper.getRequest().getSession().removeAttribute("mobileExamCaseStep");
    }

}
