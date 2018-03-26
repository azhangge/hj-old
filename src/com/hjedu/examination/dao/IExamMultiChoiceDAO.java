package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamMultiChoice;

public interface IExamMultiChoiceDAO {

    public abstract void addExamMultiChoice(ExamMultiChoice m);

    public abstract void updateExamMultiChoice(ExamMultiChoice m);

    public abstract void deleteExamMultiChoice(String id);

    public abstract ExamMultiChoice findExamMultiChoice(String id);

    public abstract List<ExamMultiChoice> findAllExamMultiChoice();

    public abstract List<ExamMultiChoice> findExamMultiChoiceByQuestion(String questionId);
}
