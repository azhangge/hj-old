package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IModuleBuffetPartDAO;
import com.hjedu.examination.entity.buffet.ModuleBuffetPart;

public class ModuleBuffetPartDAO implements IModuleBuffetPartDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void addModuleBuffetPart(ModuleBuffetPart m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteModuleBuffetPart(String id) {
        ModuleBuffetPart c = this.entityManager.find(ModuleBuffetPart.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<ModuleBuffetPart> findAllModuleBuffetPart() {
        String q = "Select cq from ModuleBuffetPart cq";
        List<ModuleBuffetPart> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ModuleBuffetPart findModuleBuffetPartByExamAndModule(String examId, String moduleId) {
        String q = "Select cq from ModuleBuffetPart cq where cq.exam.id=:examId and cq.module.id=:moduleId";
        Query qu=this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        qu.setParameter("moduleId", moduleId);
        try {
            ModuleBuffetPart cqs = (ModuleBuffetPart)qu .getSingleResult();
            return cqs;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ModuleBuffetPart findModuleBuffetPart(String id) {
        ModuleBuffetPart c = this.entityManager.find(ModuleBuffetPart.class, id);
        return c;
    }

    @Override
    public List<ModuleBuffetPart> findModuleBuffetPartByExam(String id) {
        String q = "Select cq from ModuleBuffetPart cq where cq.exam.id=:qid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ModuleBuffetPart> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateModuleBuffetPart(ModuleBuffetPart m) {
        this.entityManager.merge(m);
    }

    public void deleteAllModuleBuffetPart() {
        String q = "delete from ModuleBuffetPart cq";
        this.entityManager.createQuery(q).executeUpdate();
    }
    
    public void deleteModuleBuffetPartByModule(String mid) {
        String q = "delete from ModuleBuffetPart cq where cq.module.id=:mid";
        Query qu=this.entityManager.createQuery(q);
        qu.setParameter("mid", mid);
        qu.executeUpdate();
    }
    
    public void deleteModuleBuffetPartByExam(String mid) {
        String q = "delete from ModuleBuffetPart cq where cq.exam.id=:mid";
        Query qu=this.entityManager.createQuery(q);
        qu.setParameter("mid", mid);
        qu.executeUpdate();
    }

}
