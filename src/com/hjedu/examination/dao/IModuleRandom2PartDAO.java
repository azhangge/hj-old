package com.hjedu.examination.dao;


import java.util.List;

import com.hjedu.examination.entity.random2.ModuleRandom2Part;

public interface IModuleRandom2PartDAO {

    public abstract void addModuleRandom2Part(ModuleRandom2Part m);

    public abstract void updateModuleRandom2Part(ModuleRandom2Part m);

    public abstract void deleteModuleRandom2Part(String id);

    public abstract ModuleRandom2Part findModuleRandom2Part(String id);
    
    public abstract ModuleRandom2Part findModuleRandom2PartByExamAndModule(String examId,String moduleId);

    public abstract List<ModuleRandom2Part> findAllModuleRandom2Part();
    
    public abstract List<ModuleRandom2Part> findModuleRandom2PartByExam(String examId);
    
    
    public void deleteAllModuleRandom2Part();
    
    public void deleteModuleBuffetPartByModule(String mid);
    
    public void deleteModuleBuffetPartByPaper(String mid);
    
    
}
