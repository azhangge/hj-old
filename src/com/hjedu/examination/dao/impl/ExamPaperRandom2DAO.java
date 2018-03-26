package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IExamPaperRandom2DAO;
import com.hjedu.examination.dao.IModuleRandom2PartDAO;
import com.hjedu.examination.dao.IRandom2PaperPartDAO;
import com.hjedu.examination.entity.random2.ExamPaperRandom2;
import com.hjedu.examination.entity.random2.Random2PaperPart;
import com.huajie.util.ExternalUserUtil;

public class ExamPaperRandom2DAO implements IExamPaperRandom2DAO, Serializable {
    
    @PersistenceContext
    private EntityManager entityManager;
    IModuleRandom2PartDAO mrandom2PartDAO;
    
    IRandom2PaperPartDAO partDAO;
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public IModuleRandom2PartDAO getMrandom2PartDAO() {
        return mrandom2PartDAO;
    }

    public void setMrandom2PartDAO(IModuleRandom2PartDAO mrandom2PartDAO) {
        this.mrandom2PartDAO = mrandom2PartDAO;
    }
    public IRandom2PaperPartDAO getPartDAO() {
        return partDAO;
    }
    
    public void setPartDAO(IRandom2PaperPartDAO partDAO) {
        this.partDAO = partDAO;
    }
    
    @Override
    public void addExamPaperRandom2(ExamPaperRandom2 m) {
        this.entityManager.persist(m);
    }
    
    @Override
    public void deleteExamPaperRandom2(String id) {
        this.mrandom2PartDAO.deleteModuleBuffetPartByModule(id);
        ExamPaperRandom2 c = this.entityManager.find(ExamPaperRandom2.class, id);
        List<Random2PaperPart> pts = c.getParts();
        for (Random2PaperPart e : pts) {
            this.partDAO.deleteRandom2PaperPart(e.getId());
        }        
        this.entityManager.remove(c);
    }
    
    @Override
    public List<ExamPaperRandom2> findAllExamPaperRandom2(String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q = "Select cq from ExamPaperRandom2 cq where :admin member of cq.admins and cq.businessId=:businessId order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("admin", ai);
        qu.setParameter("businessId", businessId);
        List<ExamPaperRandom2> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public ExamPaperRandom2 findExamPaperRandom2(String id) {
        ExamPaperRandom2 c = this.entityManager.find(ExamPaperRandom2.class, id);
        return c;
    }
    
    @Override
    public void updateExamPaperRandom2(ExamPaperRandom2 m) {
        
        List<Random2PaperPart> pts = m.getParts();
        for (Random2PaperPart e : pts) {
            this.partDAO.updateRandom2PaperPart(e);
        }
        this.entityManager.merge(m);
        this.entityManager.flush();
    }
}
