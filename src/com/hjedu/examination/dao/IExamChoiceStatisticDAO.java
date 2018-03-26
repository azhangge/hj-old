package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamChoiceStatistic;

public interface IExamChoiceStatisticDAO {

    public abstract void addExamChoiceStatistic(ExamChoiceStatistic m);

    public abstract void updateExamChoiceStatistic(ExamChoiceStatistic m);

    public abstract void deleteExamChoiceStatistic(String id);

    public abstract ExamChoiceStatistic findExamChoiceStatistic(String id);

    public abstract List<ExamChoiceStatistic> findAllExamChoiceStatistic();

    public abstract List<ExamChoiceStatistic> findExamChoiceStatisticByChoice(String cId);

    public long getNumByChoiceAndExam(String id, String examId);

    public void deleteStatisticByCase(String caseId);

    public void deleteStatisticByExam(String caseId);

    public void deleteStatisticByUser(String caseId);
    
    public void deleteStatisticByChoice(String caseId);
}
