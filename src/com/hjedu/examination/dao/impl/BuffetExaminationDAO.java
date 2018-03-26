package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IBuffetExaminationDAO;
import com.hjedu.examination.dao.IModuleBuffetPartDAO;
import com.hjedu.examination.entity.buffet.BuffetExamination;
import com.huajie.util.ExternalUserUtil;

public class BuffetExaminationDAO implements IBuffetExaminationDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    IModuleBuffetPartDAO mbuffetPartDAO;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IModuleBuffetPartDAO getMbuffetPartDAO() {
        return mbuffetPartDAO;
    }

    public void setMbuffetPartDAO(IModuleBuffetPartDAO mbuffetPartDAO) {
        this.mbuffetPartDAO = mbuffetPartDAO;
    }

    @Override
    public void addExamination(BuffetExamination m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamination(String id) {
        this.mbuffetPartDAO.deleteModuleBuffetPartByExam(id);
        BuffetExamination c = this.entityManager.find(BuffetExamination.class, id);
        this.entityManager.remove(c);
    }

    @Override
    public List<BuffetExamination> findAllExamination(String businessId) {
        String q = "Select cq from BuffetExamination cq where cq.id!='7' and cq.businessId=:businessId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        List<BuffetExamination> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<BuffetExamination> findAllExaminationByAdmin(String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q = "Select cq from BuffetExamination cq where cq.id!='7' and cq.businessId=:businessId and :ai member of cq.admins order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        qu.setParameter("ai", ai);
        List<BuffetExamination> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<BuffetExamination> findAllShowedExaminationByBusinessId(String businessId) {
        String q = "Select cq from BuffetExamination cq where cq.ifShow=true and cq.id!='7' and cq.businessId=:businessId order by cq.genTime desc";
        Query query = this.entityManager.createQuery(q);
        query.setParameter("businessId", businessId);
        List<BuffetExamination> cqs = query.getResultList();
        return cqs;
    }
    
    @Override
    public List<BuffetExamination> findExaminationByLabelAndBusinessId(String labelId, String businessId) {
        String q = "Select cq from BuffetExamination cq where cq.ifShow=true and cq.businessId = :businessId and cq.labelStr like '%" + labelId + "%' order by cq.genTime desc";
        Query query = this.entityManager.createQuery(q);
        query.setParameter("businessId", businessId);
        List<BuffetExamination> cqs = query.getResultList();
        return cqs;
    }

    @Override
    public BuffetExamination findExamination(String id) {
        BuffetExamination c = this.entityManager.find(BuffetExamination.class, id);
        return c;
    }

    @Override
    public void updateExamination(BuffetExamination m) {
        this.entityManager.merge(m);
    }
}
