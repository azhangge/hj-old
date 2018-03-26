package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.buffet.ModuleBuffetPart;

public interface IModuleBuffetPartDAO {

    public abstract void addModuleBuffetPart(ModuleBuffetPart m);

    public abstract void updateModuleBuffetPart(ModuleBuffetPart m);

    public abstract void deleteModuleBuffetPart(String id);

    public abstract ModuleBuffetPart findModuleBuffetPart(String id);

    public abstract ModuleBuffetPart findModuleBuffetPartByExamAndModule(String examId, String moduleId);

    public abstract List<ModuleBuffetPart> findAllModuleBuffetPart();

    public abstract List<ModuleBuffetPart> findModuleBuffetPartByExam(String examId);

    public void deleteAllModuleBuffetPart();

    public void deleteModuleBuffetPartByModule(String mid);

    public void deleteModuleBuffetPartByExam(String mid);
}
