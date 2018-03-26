package com.hjedu.examination.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamPaperManualPart;
import com.hjedu.examination.entity.Examination;
import com.hjedu.examination.entity.ManualPartItem;
import com.hjedu.examination.service.impl.ExamChoiceStatisticService;
import com.hjedu.platform.service.ILogService;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ViewExamStatistic implements Serializable {

    ILogService logger = SpringHelper.getSpringBean("LogService");
    IExaminationDAO examDAO = SpringHelper.getSpringBean("ExaminationDAO");
    IExamCaseDAO examCaseDAO = SpringHelper.getSpringBean("ExamCaseDAO");
    ExamChoiceStatisticService statService = SpringHelper.getSpringBean("ExamChoiceStatisticService");

    Examination exam;

    List<ChoiceQuestion> choices = new ArrayList();

    public Examination getExam() {
        return exam;
    }

    public void setExam(Examination exam) {
        this.exam = exam;
    }

    public List<ChoiceQuestion> getChoices() {
        return choices;
    }

    public void setChoices(List<ChoiceQuestion> choices) {
        this.choices = choices;
    }

    @PostConstruct
    public void init() {
        HttpServletRequest request = JsfHelper.getRequest();
        String id = request.getParameter("id");
        if (id != null) {
            this.exam = this.examDAO.findExamination(id);
            this.loadQuestions();
        }

    }

    public void loadQuestions() {
        if (exam.getPaperType().equals("manual")) {
            List<ExamPaperManualPart> parts = this.exam.getManualPaper().getParts();
            for (ExamPaperManualPart part : parts) {
                List<ManualPartItem> items = part.getItems();
                for (ManualPartItem item : items) {
                    if (item.getQtype().equals("choice")) {
                        this.choices.add((ChoiceQuestion) item.getQuestion());
                    }
                }
            }

        } else {
            Set sets = new HashSet();
            List<ExamCase> cases = examCaseDAO.findExamCaseByExamination(exam.getId());
            for (ExamCase ec : cases) {
                List<ExamCaseItemChoice> chis = ec.getChoiceItems();
                for (ExamCaseItemChoice chi : chis) {
                    sets.add(chi.getQuestion());
                }
            }
            this.choices.addAll(sets);
        }
        statService.loadStatistic(choices, exam.getId());

    }

}
