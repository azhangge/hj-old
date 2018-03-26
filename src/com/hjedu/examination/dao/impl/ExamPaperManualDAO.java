package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IExamPaperManualDAO;
import com.hjedu.examination.dao.IExamPaperManualPartDAO;
import com.hjedu.examination.entity.ExamPaperManual;
import com.hjedu.examination.entity.ExamPaperManualPart;
import com.huajie.util.ExternalUserUtil;

public class ExamPaperManualDAO implements IExamPaperManualDAO, Serializable {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    IExamPaperManualPartDAO partDAO;
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public IExamPaperManualPartDAO getPartDAO() {
        return partDAO;
    }
    
    public void setPartDAO(IExamPaperManualPartDAO partDAO) {
        this.partDAO = partDAO;
    }
    
    @Override
    public void addExamPaperManual(ExamPaperManual m) {
        this.entityManager.persist(m);
    }
    
    @Override
    public void deleteExamPaperManual(String id) {
        ExamPaperManual c = this.entityManager.find(ExamPaperManual.class, id);
        List<ExamPaperManualPart> pts = c.getParts();
        for (ExamPaperManualPart e : pts) {
            this.partDAO.deleteExamPaperManualPart(e.getId());
        }        
        this.entityManager.remove(c);
    }
    
    @Override
    public List<ExamPaperManual> findAllExamPaperManual(String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q = "Select cq from ExamPaperManual cq where :admin member of cq.admins and cq.businessId=:businessId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("admin", ai);
        qu.setParameter("businessId", businessId);
        List<ExamPaperManual> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public ExamPaperManual findExamPaperManual(String id) {
        ExamPaperManual c = this.entityManager.find(ExamPaperManual.class, id);
        return c;
    }
    
    @Override
    public void updateExamPaperManual(ExamPaperManual m) {
        
        List<ExamPaperManualPart> pts = m.getParts();
        for (ExamPaperManualPart e : pts) {
            this.partDAO.updateExamPaperManualPart(e);
        }
        this.entityManager.merge(m);
        this.entityManager.flush();
    }
}
