package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamLabel;

public interface IExamLabelDAO {

    public abstract void addExamLabel(ExamLabel m);

    public abstract void updateExamLabel(ExamLabel m);

    public abstract void deleteExamLabel(String id);

    public abstract ExamLabel findExamLabel(String id);

    public abstract List<ExamLabel> findAllExamLabel();
    
    public List<ExamLabel> findAllShowedExamLabel();
    
    public List<ExamLabel> findAllShowedExamLabelByType(String typeId);
    
    public List<ExamLabel> findMostExamLabelByType(String typeId);
    
    

}
