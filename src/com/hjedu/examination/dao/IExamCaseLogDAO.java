package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamCaseLog;

public interface IExamCaseLogDAO {

    public List<ExamCaseLog> findExamCaseLogByUser(String userId, String examinationId);
    
    public ExamCaseLog findExamCaseLog(String id);

    public ExamCaseLog findLatestExamCaseLogByUser(String userId, String examinationId);
    
    public long findNumExamCaseLogByUser(String userId, String examinationId); 

    public void addExamCaseLog(ExamCaseLog log);

    public List<ExamCaseLog> findAllExamCaseLog();

    public void deleteExamCaseLog(String id);

    public void deleteAllExamCaseLog();
    
    public void deleteExamCaseLogByUser(String uid);
    
    public void deleteExamCaseLogByExam(String uid);
}
