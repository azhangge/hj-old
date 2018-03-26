package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IModuleRandom2PartDAO;
import com.hjedu.examination.entity.random2.ModuleRandom2Part;

public class ModuleRandom2PartDAO implements IModuleRandom2PartDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addModuleRandom2Part(ModuleRandom2Part m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteModuleRandom2Part(String id) {
        ModuleRandom2Part c = this.entityManager.find(ModuleRandom2Part.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<ModuleRandom2Part> findAllModuleRandom2Part() {
        String q = "Select cq from ModuleRandom2Part cq";
        List<ModuleRandom2Part> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ModuleRandom2Part findModuleRandom2PartByExamAndModule(String examId, String moduleId) {
        String q = "Select cq from ModuleRandom2Part cq where cq.paper.id=:examId and cq.module.id=:moduleId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        qu.setParameter("moduleId", moduleId);
        try {
            ModuleRandom2Part cqs = (ModuleRandom2Part) qu.getSingleResult();
            return cqs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ModuleRandom2Part findModuleRandom2Part(String id) {
        ModuleRandom2Part c = this.entityManager.find(ModuleRandom2Part.class, id);
        return c;
    }

    @Override
    public List<ModuleRandom2Part> findModuleRandom2PartByExam(String id) {
        String q = "Select cq from ModuleRandom2Part cq where cq.paper.id=:qid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ModuleRandom2Part> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateModuleRandom2Part(ModuleRandom2Part m) {
        this.entityManager.merge(m);
    }

    @Override
    public void deleteAllModuleRandom2Part() {
        String q = "delete from ModuleRandom2Part cq";
        this.entityManager.createQuery(q).executeUpdate();
    }
    
    public void deleteModuleBuffetPartByModule(String mid) {
        String q = "delete from ModuleRandom2Part cq where cq.module.id=:mid";
        Query qu=this.entityManager.createQuery(q);
        qu.setParameter("mid", mid);
        qu.executeUpdate();
    }
    
    public void deleteModuleBuffetPartByPaper(String mid) {
        String q = "delete from ModuleRandom2Part cq where cq.paper.id=:mid";
        Query qu=this.entityManager.createQuery(q);
        qu.setParameter("mid", mid);
        qu.executeUpdate();
    }

}
