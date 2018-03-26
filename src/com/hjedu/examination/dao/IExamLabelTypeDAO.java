package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamLabelType;


public interface IExamLabelTypeDAO {
    
  public abstract void addExamLabelType(ExamLabelType paramExamLabelType);

  public abstract ExamLabelType findExamLabelType(String paramString);

  public abstract void updateExamLabelType(ExamLabelType paramExamLabelType);

  public abstract List<ExamLabelType> findAllExamLabelTypeByBusinessId(String businessId);

  public abstract void deleteExamLabelType(String paramString);
}
