package com.hjedu.examination.dao;


import java.util.List;

import com.hjedu.examination.entity.module2.ModuleExam2Part;

public interface IModuleExamPartDAO {

    public abstract void addModuleExamPart(ModuleExam2Part m);

    public abstract void updateModuleExamPart(ModuleExam2Part m);

    public abstract void deleteModuleExamPart(String id);

    public abstract ModuleExam2Part findModuleExamPart(String id);

    public abstract List<ModuleExam2Part> findAllModuleExamPart();
    
    public List<ModuleExam2Part> findAllModuleExamPartByExam(String pid);

}
