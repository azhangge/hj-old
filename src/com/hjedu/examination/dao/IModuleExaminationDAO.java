package com.hjedu.examination.dao;


import java.util.List;

import com.hjedu.examination.entity.module2.ModuleExamination2;

public interface IModuleExaminationDAO {

    public abstract void addExamination(ModuleExamination2 m);

    public abstract void updateExamination(ModuleExamination2 m);

    public abstract void deleteExamination(String id);

    public abstract ModuleExamination2 findExamination(String id);
    
    public ModuleExamination2 findExaminationByModule(String id);

    public abstract List<ModuleExamination2> findAllExamination();
    
    public List<ModuleExamination2> findAllShowedExamination();

}
