package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamChoice;

public interface IExamChoiceDAO {

    public abstract void addExamChoice(ExamChoice m);

    public abstract void updateExamChoice(ExamChoice m);

    public abstract void deleteExamChoice(String id);

    public abstract ExamChoice findExamChoice(String id);

    public abstract List<ExamChoice> findAllExamChoice();

    public abstract List<ExamChoice> findExamChoiceByQuestion(String questionId);
}
