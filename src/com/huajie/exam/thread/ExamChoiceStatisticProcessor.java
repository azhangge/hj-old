package com.huajie.exam.thread;

import java.util.List;

import com.hjedu.examination.dao.IExamChoiceStatisticDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamChoice;
import com.hjedu.examination.entity.ExamChoiceStatistic;
import com.huajie.util.SpringHelper;

public class ExamChoiceStatisticProcessor implements Runnable {
    
    private IExamChoiceStatisticDAO sDAO = SpringHelper.getSpringBean("ExamChoiceStatisticDAO");
    private ExamCase examCase;
    
    public ExamChoiceStatisticProcessor(ExamCase ec) {
        this.examCase = ec;
    }
    
    @Override
    public void run() {
        this.processExamCase(examCase);
    }
    
    private void processExamCase(ExamCase ec) {
        List<ExamCaseItemChoice> ecis = ec.getChoiceItems();
        //System.out.println("choice question num ready to be add to statistic:"+ecis.size());
        for (ExamCaseItemChoice eci : ecis) {
            //System.out.println("user answer:"+eci.getUserAnswer());
            List<ExamChoice> ecs = eci.getQuestion().getChoices();
            for (ExamChoice ecc : ecs) {
                if (ecc.getLabel().trim().equals(eci.getUserAnswer())) {
                    ExamChoiceStatistic et = new ExamChoiceStatistic();
                    et.setChoice(ecc);
                    et.setExamCaseId(examCase.getId());
                    et.setExam(examCase.getExamination());
                    et.setUser(examCase.getUser());
                    sDAO.addExamChoiceStatistic(et);
                    break;
                }
            }
            
        }
        
    }
    
}
