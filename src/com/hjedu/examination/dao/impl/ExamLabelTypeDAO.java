package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.examination.dao.IExamLabelTypeDAO;
import com.hjedu.examination.entity.ExamLabelType;

public class ExamLabelTypeDAO implements IExamLabelTypeDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamLabelType(ExamLabelType paramExamLabelType) {
        this.entityManager.persist(paramExamLabelType);
    }

    @Override
    public void deleteExamLabelType(String paramString) {
        ExamLabelType yp = (ExamLabelType) this.entityManager.find(ExamLabelType.class, paramString);
        this.entityManager.remove(yp);
    }

    @Override
    public List<ExamLabelType> findAllExamLabelTypeByBusinessId(String businessId) {
        String q = "select yis from ExamLabelType yis where yis.businessId = :businessId order by yis.ord";
        Query query = entityManager.createQuery(q);
        query.setParameter("businessId", businessId);
        List<ExamLabelType> ps = query.getResultList();
        return ps;
    }

    @Override
    public ExamLabelType findExamLabelType(String paramString) {
        ExamLabelType p = (ExamLabelType) this.entityManager.find(ExamLabelType.class, paramString);
        return p;
    }

    @Override
    public void updateExamLabelType(ExamLabelType paramExamLabelType) {
        this.entityManager.merge(paramExamLabelType);
    }
}
