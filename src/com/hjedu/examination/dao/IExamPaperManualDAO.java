package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamPaperManual;

public interface IExamPaperManualDAO {

    public abstract void addExamPaperManual(ExamPaperManual m);

    public abstract void updateExamPaperManual(ExamPaperManual m);

    public abstract void deleteExamPaperManual(String id);

    public abstract ExamPaperManual findExamPaperManual(String id);

    public abstract List<ExamPaperManual> findAllExamPaperManual(String businessId);

}
