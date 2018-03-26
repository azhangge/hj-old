
package com.hjedu.examination.controller;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamPaperManualDAO;
import com.hjedu.examination.dao.IExamPaperRandom2DAO;
import com.hjedu.examination.dao.IExamPaperRandomDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamPaperManual;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.examination.service.IExamCaseService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

/**
 *
 * @author huajie
 */
@ManagedBean
@ViewScoped
public class PaperDemoGeneral {

    IExamCaseService examCaseService;
 
    IExamPaperRandomDAO paperDAO = SpringHelper.getSpringBean("ExamPaperRandomDAO");
    IExamPaperManualDAO mpaperDAO = SpringHelper.getSpringBean("ExamPaperManualDAO");
    IExamPaperRandom2DAO rpaperDAO = SpringHelper.getSpringBean("ExamPaperRandom2DAO");
    ExamCase examCase = new ExamCase();

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
        String ptype = request.getParameter("ptype");

        if (pid != null && ptype != null) {
            Examination exam = new Examination();
            if ("random".equals(ptype)) {
                examCaseService = SpringHelper.getSpringBean("ExamCaseService");
                ExamPaperRandom randomPaper = this.paperDAO.findExamPaperRandom(pid);
                exam.setName(randomPaper.getName());
                exam.setRandomPaper(randomPaper);
            } else if ("random2".equals(ptype)) {
                examCaseService = SpringHelper.getSpringBean("ExamCaseRandom2Service");
                ExamPaperRandom2 randomPaper2 = this.rpaperDAO.findExamPaperRandom2(pid);
                exam.setName(randomPaper2.getName());
                exam.setRandom2Paper(randomPaper2);
            } else if ("manual".equals(ptype)) {
                examCaseService = SpringHelper.getSpringBean("ManualExamCaseService");
                ExamPaperManual paper = this.mpaperDAO.findExamPaperManual(pid);
                exam.setName(paper.getName());
                exam.setManualPaper(paper);
            }
            this.examCase.setExamination(exam);
            this.examCaseService.buildExamCase(examCase);
            this.examCase.setIp(request.getRemoteAddr());
        }
    }

}
