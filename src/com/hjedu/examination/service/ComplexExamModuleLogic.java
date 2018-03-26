package com.hjedu.examination.service;


import java.util.List;

import com.hjedu.examination.entity.ExamModuleModel;


public interface ComplexExamModuleLogic {

    public List<ExamModuleModel> buildQulifiedStructureByDic(String fid, List<ExamModuleModel> files);

    public List<ExamModuleModel> buildStructure(List<ExamModuleModel> cfs1);

    public List<ExamModuleModel> buildStructure(String fid, List<ExamModuleModel> cfs1);
    
    public String genComplexGrapaID(String fatherId,List<ExamModuleModel> files) ;
    
    
    public List<ExamModuleModel> genFamilyTree(String id);
    
    public List<ExamModuleModel> genComplexFamilyTree(String id, List<ExamModuleModel> files);

    public String genAncestors(String id);
    
    
}
