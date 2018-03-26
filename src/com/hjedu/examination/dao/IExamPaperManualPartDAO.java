package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamPaperManualPart;

public interface IExamPaperManualPartDAO {

    public abstract void addExamPaperManualPart(ExamPaperManualPart m);

    public abstract void updateExamPaperManualPart(ExamPaperManualPart m);

    public abstract void deleteExamPaperManualPart(String id);

    public abstract ExamPaperManualPart findExamPaperManualPart(String id);

    public abstract List<ExamPaperManualPart> findAllExamPaperManualPart();
    
    public List<ExamPaperManualPart> findAllExamPaperManualPartByPaper(String pid);

}
