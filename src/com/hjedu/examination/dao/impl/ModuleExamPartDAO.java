package com.hjedu.examination.dao.impl;


import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IModuleExamPartDAO;
import com.hjedu.examination.entity.module2.ModuleExam2Part;

public class ModuleExamPartDAO implements IModuleExamPartDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addModuleExamPart(ModuleExam2Part m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteModuleExamPart(String id) {
        ModuleExam2Part c = this.entityManager.find(ModuleExam2Part.class, id);
        if (c != null) {
            this.entityManager.remove(c);
        }
    }

    @Override
    public List<ModuleExam2Part> findAllModuleExamPart() {
        String q = "Select cq from ModuleExam2Part cq order by cq.genTime desc";
        List<ModuleExam2Part> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ModuleExam2Part> findAllModuleExamPartByExam(String pid) {
        String q = "Select cq from ModuleExam2Part cq where cq.exam.id=:pid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("pid", pid);
        List<ModuleExam2Part> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public ModuleExam2Part findModuleExamPart(String id) {
        ModuleExam2Part c = this.entityManager.find(ModuleExam2Part.class, id);
        return c;
    }

    @Override
    public void updateModuleExamPart(ModuleExam2Part m) {
        this.entityManager.merge(m);
    }
}
