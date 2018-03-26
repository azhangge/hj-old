package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamPaperRandomDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class RandomPaperDemo implements Serializable {

    IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IExamCaseService examCaseService = SpringHelper.getSpringBean("ExamCaseService");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");

    IExamPaperRandomDAO paperDAO = SpringHelper.getSpringBean("ExamPaperRandomDAO");
    ExamCase examCase = new ExamCase();
    ExamPaperRandom randomPaper;

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
            this.randomPaper = this.paperDAO.findExamPaperRandom(pid);
            Examination exam;
            if (eid != null) {
                exam = this.examinationDAO.findExamination(eid);
            } else {
                exam = new Examination();
                exam.setName(randomPaper.getName());
            }
            exam.setRandomPaper(this.paperDAO.findExamPaperRandom(pid));
            this.examCase.setExamination(exam);
            this.examCaseService.buildExamCase(examCase);
        }

        this.examCase.setIp(request.getRemoteAddr());

    }

}
