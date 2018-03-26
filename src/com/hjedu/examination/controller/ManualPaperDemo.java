package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamPaperManualDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamPaperManual;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ManualPaperDemo implements Serializable {

    IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IExamCaseService examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    IExamPaperManualDAO paperDAO = SpringHelper.getSpringBean("ExamPaperManualDAO");
    //ClientSession cs = JsfHelper.getBean("clientSession");
    ExamCase examCase = new ExamCase();

    ExamPaperManual manualPaper;

    public ExamCase getExamCase() {
        return examCase;
    }

    public void setExamCase(ExamCase examCase) {
        this.examCase = examCase;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String pid = request.getParameter("pid");
        String eid = request.getParameter("eid");
        if (pid != null) {
            this.manualPaper = this.paperDAO.findExamPaperManual(pid);
            Examination exam;
            if (eid != null) {
                exam = this.examinationDAO.findExamination(eid);
            } else {
                exam = new Examination();
                exam.setName(manualPaper.getName());
            }
            exam.setManualPaper(this.paperDAO.findExamPaperManual(pid));
            this.examCase.setExamination(exam);
            this.examCaseService.buildExamCase(examCase);
        }

        this.examCase.setIp(request.getRemoteAddr());
    }

}
