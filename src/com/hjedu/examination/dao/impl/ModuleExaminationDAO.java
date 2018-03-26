package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IModuleExaminationDAO;
import com.hjedu.examination.dao.IModuleModule2PartDAO;
import com.hjedu.examination.entity.module2.ModuleExamination2;

public class ModuleExaminationDAO implements IModuleExaminationDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    IModuleModule2PartDAO mmPartDAO;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamination(ModuleExamination2 m) {
        this.entityManager.persist(m);
    }

    public IModuleModule2PartDAO getMmPartDAO() {
        return mmPartDAO;
    }

    public void setMmPartDAO(IModuleModule2PartDAO mmPartDAO) {
        this.mmPartDAO = mmPartDAO;
    }
    

    @Override
    public void deleteExamination(String id) {
        this.mmPartDAO.deleteModuleModule2PartByExam(id);
        ModuleExamination2 c = this.entityManager.find(ModuleExamination2.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<ModuleExamination2> findAllExamination() {
        String q = "Select cq from ModuleExamination2 cq order by cq.genTime desc";
        List<ModuleExamination2> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public ModuleExamination2 findExaminationByModule(String id) {
        String q = "Select cq from ModuleExamination2 cq where cq.module.id=:id order by cq.genTime desc";
        Query qu=this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        ModuleExamination2 cqs = (ModuleExamination2)qu.getSingleResult();
        return cqs;
    }

    @Override
    public List<ModuleExamination2> findAllShowedExamination() {
        String q = "Select cq from ModuleExamination2 cq where cq.ifShow=true  order by cq.genTime desc";
        List<ModuleExamination2> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ModuleExamination2 findExamination(String id) {
        ModuleExamination2 c = this.entityManager.find(ModuleExamination2.class, id);
        return c;
    }

    @Override
    public void updateExamination(ModuleExamination2 m) {
        this.entityManager.merge(m);
    }
}
