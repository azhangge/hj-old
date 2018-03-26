package com.hjedu.examination.service.impl;

import java.io.Serializable;
import java.util.List;

import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseItemChoice;
import com.hjedu.examination.entity.ExamChoice;

public class ExamChoiceStatisticService implements Serializable{

    public void loadStatistic(ExamCase ec) {
        List<ExamCaseItemChoice> ecis = ec.getChoiceItems();
        for (ExamCaseItemChoice eci : ecis) {
            List<ExamChoice> ecs = eci.getQuestion().getChoices();
            for (ExamChoice ecc : ecs) {
                ecc.fetchSelectNum(ec.getExamination().getId());//整理每一个选项的选择次数
            }
            eci.getQuestion().fetchChoicesTotalSelectNum();//整理选择题的选择总次数
        }
    }

    public void loadStatistic(ChoiceQuestion question, String examId) {
        List<ExamChoice> ecs = question.getChoices();
        for (ExamChoice ecc : ecs) {
            ecc.fetchSelectNum(examId);//整理每一个选项的选择次数
        }
        question.fetchChoicesTotalSelectNum();//整理选择题的选择总次数
    }

    public void loadStatistic(List<ChoiceQuestion> questions, String examId) {
        for (ChoiceQuestion question : questions) {
            List<ExamChoice> ecs = question.getChoices();
            for (ExamChoice ecc : ecs) {
                ecc.fetchSelectNum(examId);//整理每一个选项的选择次数
            }
            question.fetchChoicesTotalSelectNum();//整理选择题的选择总次数
        }
    }

}
