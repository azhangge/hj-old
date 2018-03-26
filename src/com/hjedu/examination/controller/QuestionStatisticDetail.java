package com.hjedu.examination.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.service.impl.ExamChoiceStatisticService;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class QuestionStatisticDetail implements Serializable {

    ExamChoiceStatisticService statService = SpringHelper.getSpringBean("ExamChoiceStatisticService");
    IChoiceQuestionDAO cqDAO = SpringHelper.getSpringBean("ChoiceQuestionDAO");
    
    ChoiceQuestion choiceQuestion;

    public ChoiceQuestion getChoiceQuestion() {
        return choiceQuestion;
    }

    public void setChoiceQuestion(ChoiceQuestion choiceQuestion) {
        this.choiceQuestion = choiceQuestion;
    }

    


    
    
    @PostConstruct
    public void init() {

    }

    public String viewStatistic1(String qid,String examId) {
        choiceQuestion=cqDAO.findChoiceQuestion(qid);
        this.statService.loadStatistic(choiceQuestion, examId);
        return null;
    }

    
}
