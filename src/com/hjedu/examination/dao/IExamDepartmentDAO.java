package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.ExamDepartment;

public interface IExamDepartmentDAO {

    public abstract void addExamDepartment(ExamDepartment m);

    public abstract void updateExamDepartment(ExamDepartment m);

    public abstract void deleteExamDepartment(String id);

    public abstract ExamDepartment findExamDepartment(String id);

    public abstract List<ExamDepartment> findAllExamDepartment(String rootId);
    
    public List<ExamDepartment> findRootExamDepartment(String rootId) ;
    
    public List<ExamDepartment> findAllShowedExamDepartment(String rootId);

}
