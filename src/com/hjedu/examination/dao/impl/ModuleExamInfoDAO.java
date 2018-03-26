package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.hjedu.examination.dao.IModuleExamInfoDAO;
import com.hjedu.examination.entity.module.ModuleExamInfo;

public class ModuleExamInfoDAO implements IModuleExamInfoDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public void updateModuleExamInfo(ModuleExamInfo m) {
        this.entityManager.merge(m);
    }

    @Override
    public ModuleExamInfo findModuleExamInfo() {
        String q = "Select cq from ModuleExamInfo cq";
        ModuleExamInfo cqs = null;
        try {
            cqs = (ModuleExamInfo) this.entityManager.createQuery(q).getSingleResult();
        } catch (Exception e) {
        }
        if (cqs == null) {
            return new ModuleExamInfo();
        } else {
            return cqs;
        }
    }
}
