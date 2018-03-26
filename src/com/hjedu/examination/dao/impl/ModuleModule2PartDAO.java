package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IModuleModule2PartDAO;
import com.hjedu.examination.entity.module2.ModuleModule2Part;

public class ModuleModule2PartDAO implements IModuleModule2PartDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void addModuleModule2Part(ModuleModule2Part m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteModuleModule2Part(String id) {
        ModuleModule2Part c = this.entityManager.find(ModuleModule2Part.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<ModuleModule2Part> findAllModuleModule2Part() {
        String q = "Select cq from ModuleModule2Part cq";
        List<ModuleModule2Part> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ModuleModule2Part findModuleModule2PartByExamAndModule(String examId, String moduleId) {
        String q = "Select cq from ModuleModule2Part cq where cq.exam.id=:examId and cq.module.id=:moduleId";
        Query qu=this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        qu.setParameter("moduleId", moduleId);
        try {
            ModuleModule2Part cqs = (ModuleModule2Part)qu .getSingleResult();
            return cqs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ModuleModule2Part findModuleModule2Part(String id) {
        ModuleModule2Part c = this.entityManager.find(ModuleModule2Part.class, id);
        return c;
    }

    @Override
    public List<ModuleModule2Part> findModuleModule2PartByExam(String id) {
        String q = "Select cq from ModuleModule2Part cq where cq.exam.id=:qid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ModuleModule2Part> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateModuleModule2Part(ModuleModule2Part m) {
        this.entityManager.merge(m);
    }

    public void deleteAllModuleModule2Part() {
        String q = "delete from ModuleModule2Part cq";
        this.entityManager.createQuery(q).executeUpdate();
    }
    
    public void deleteModuleModule2PartByModule(String mid) {
        String q = "delete from ModuleModule2Part cq where cq.module.id=:mid";
        Query qu=this.entityManager.createQuery(q);
        qu.setParameter("mid", mid);
        qu.executeUpdate();
    }
    
    public void deleteModuleModule2PartByExam(String mid) {
        String q = "delete from ModuleModule2Part cq where cq.exam.id=:mid";
        Query qu=this.entityManager.createQuery(q);
        qu.setParameter("mid", mid);
        qu.executeUpdate();
    }

}
