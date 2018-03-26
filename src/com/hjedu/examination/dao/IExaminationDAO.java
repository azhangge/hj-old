package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.Examination;

public interface IExaminationDAO {

    public abstract void addExamination(Examination m);

    public abstract void updateExamination(Examination m);

    public abstract void deleteExamination(String id);

    public abstract Examination findExamination(String id);

    public abstract List<Examination> findAllExamination(String businessId);
    
    public List<Examination> findAllExternalExamination();
    
    public Examination findSingleExternalExamination();
    
    public List<Examination> findExaminationByLabel(String labelId);
    
    public List<Examination> findExaminationByDept(String deptId);
    
    public List<Examination> findExaminationByLabelAndDept(String labelId,String deptId) ;
    
    public List<Examination> findAllShowedExamination();
    
    public long getExaminationNum();

	public abstract List<Examination> findExamsByIdList(List<String> idList);

	public List<Examination> findAllntensiveExamination();

	public List<Examination> findAllntensiveNoEndExamination();

	List<Examination> findAvailableExamination();
}
