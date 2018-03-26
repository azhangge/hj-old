package com.hjedu.examination.dao;


import java.util.List;

import com.hjedu.examination.entity.module2.ModuleModule2Part;

public interface IModuleModule2PartDAO {

    public abstract void addModuleModule2Part(ModuleModule2Part m);

    public abstract void updateModuleModule2Part(ModuleModule2Part m);

    public abstract void deleteModuleModule2Part(String id);

    public abstract ModuleModule2Part findModuleModule2Part(String id);

    public abstract ModuleModule2Part findModuleModule2PartByExamAndModule(String examId, String moduleId);

    public abstract List<ModuleModule2Part> findAllModuleModule2Part();

    public abstract List<ModuleModule2Part> findModuleModule2PartByExam(String examId);

    public void deleteAllModuleModule2Part();

    public void deleteModuleModule2PartByModule(String mid);

    public void deleteModuleModule2PartByExam(String mid);
}
