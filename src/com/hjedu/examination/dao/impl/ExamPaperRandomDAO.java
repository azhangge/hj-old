package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.alipay.api.domain.BusinessBankAccountInfo;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IExamPaperRandomDAO;
import com.hjedu.examination.entity.ExamPaperRandom;
import com.huajie.util.ExternalUserUtil;

public class ExamPaperRandomDAO implements IExamPaperRandomDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addExamPaperRandom(ExamPaperRandom m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamPaperRandom(String id) {
        ExamPaperRandom c = this.entityManager.find(ExamPaperRandom.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<ExamPaperRandom> findAllExamPaperRandom(String buisnessId) {
        String q = "Select cq from ExamPaperRandom cq where cq.businessId=:businessId order by cq.genTime desc";
        Query qu=this.entityManager.createQuery(q);
		qu.setParameter("businessId", buisnessId);
        List<ExamPaperRandom> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ExamPaperRandom> findAllExamPaperRandomByAdmin(String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q = "Select cq from ExamPaperRandom cq where :admin member of cq.admins and cq.businessId=:businessId order by cq.genTime desc";
        Query qu=this.entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
		qu.setParameter("admin", ai);
        List<ExamPaperRandom> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public ExamPaperRandom findExamPaperRandom(String id) {
        ExamPaperRandom c = this.entityManager.find(ExamPaperRandom.class, id);
        return c;
    }

    @Override
    public void updateExamPaperRandom(ExamPaperRandom m) {
        this.entityManager.merge(m);
    }
}
