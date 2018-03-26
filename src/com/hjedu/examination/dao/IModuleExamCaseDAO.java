package com.hjedu.examination.dao;

import java.util.List;
import java.util.Map;

import com.hjedu.examination.entity.module.ModuleExamCase;

public interface IModuleExamCaseDAO {

    public abstract void addModuleExamCase(ModuleExamCase m);

    public abstract void updateModuleExamCase(ModuleExamCase m);

    public abstract void deleteModuleExamCase(String id);

    public abstract ModuleExamCase findModuleExamCase(String id);

    public abstract List<ModuleExamCase> findAllModuleExamCase();
    
    public abstract List<ModuleExamCase> findAllSubmittedModuleExamCase();

    public abstract List<ModuleExamCase> findModuleExamCaseByExamination(String examId);
    
    public List<ModuleExamCase> findSubmittedModuleExamCaseByExamination(String id);
    
    public abstract List<ModuleExamCase> findModuleExamCaseByUser(String userId);
    
    public ModuleExamCase findModuleExamCaseByExaminationAndUser(String questionId,String uid);
    
    public long getParticipateNumByExamAndUser(String examId,String uid);
    
    public List<ModuleExamCase> findLotsOfExamCase(int offSet, int num,String businessId);
    
    public List<ModuleExamCase> findOrderedExamCase(int offSet, int num,String field,String type,String businessId);
    
    public List<ModuleExamCase> findExamCaseByLike( Map<String, Object> fms,String businessId);
    
    public List<ModuleExamCase> findModuleExamCase2ByUser(String userId);
    
    public long getParticipateNumByExam2(String examId) ;
    
    public long getExamCaseNum(String businessId) ;
    
    public void deleteAllModuleExamCase();
    
    public void deleteModuleExamCaseByExam(String eid);
    
    
}
