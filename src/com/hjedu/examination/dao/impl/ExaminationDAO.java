package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.course.entity.LessonLog;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamCaseLogDAO;
import com.hjedu.examination.dao.IExamChoiceStatisticDAO;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.Examination;

public class ExaminationDAO implements IExaminationDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    private IExamChoiceStatisticDAO statisticDAO;
    private IExamCaseDAO caseDAO;
    private IExamCaseLogDAO logDAO;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IExamCaseLogDAO getLogDAO() {
        return logDAO;
    }

    public void setLogDAO(IExamCaseLogDAO logDAO) {
        this.logDAO = logDAO;
    }

    public IExamChoiceStatisticDAO getStatisticDAO() {
        return statisticDAO;
    }

    public IExamCaseDAO getCaseDAO() {
        return caseDAO;
    }

    public void setCaseDAO(IExamCaseDAO caseDAO) {
        this.caseDAO = caseDAO;
    }

    public void setStatisticDAO(IExamChoiceStatisticDAO statisticDAO) {
        this.statisticDAO = statisticDAO;
    }

    @Override
    public void addExamination(Examination m) {
        this.entityManager.persist(m);
    }

    @Override
    public void deleteExamination(String id) {
        caseDAO.deleteBulkExamCaseByExam(id);
        this.logDAO.deleteExamCaseLogByExam(id);
        this.statisticDAO.deleteStatisticByExam(id);
        Examination c = this.entityManager.find(Examination.class, id);
        this.entityManager.remove(c);
    }
    /**
     * id='7'的是错题练习，过滤掉
     */
    @Override
    public List<Examination> findAllExamination(String businessId) {
        String q = "Select cq from Examination cq where cq.businessId=:businessId and cq.id!='7' and cq.ifExternal=false order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        List<Examination> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<Examination> findAllExternalExamination() {
        String q = "Select cq from Examination cq where cq.id!='7' and cq.ifExternal=true order by cq.genTime desc";
        List<Examination> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<Examination> findAllntensiveExamination() {
        String q = "Select cq from Examination cq where cq.examType='1' order by cq.genTime desc";
        List<Examination> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public List<Examination> findAllntensiveNoEndExamination() {
        String q = "Select cq from Examination cq where cq.examType='1' and cq.availableEnd>=:now order by cq.genTime desc";        
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("now", new Date());
         List<Examination> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<Examination> findAvailableExamination() {
        String q = "Select cq from Examination cq where cq.id!='7' and cq.availableEnd>=:now and cq.availableBegain<=:now order by cq.genTime desc";        
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("now", new Date());
        List<Examination> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public Examination findSingleExternalExamination() {
        String q = "Select cq from Examination cq where cq.id!='7' and cq.ifExternal=true order by cq.genTime desc";
        List<Examination> cqs = this.entityManager.createQuery(q).getResultList();
        if (cqs != null) {
            if (!cqs.isEmpty()) {
                return cqs.get(0);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<Examination> findExaminationByLabel(String labelId) {
        String q = "Select cq from Examination cq where cq.id!='7' and cq.ifShow=true  and cq.ifExternal=false  and cq.labelStr like '%" + labelId + "%' order by cq.genTime desc";
        List<Examination> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public List<Examination> findExaminationByDept(String deptId) {
        String q = "Select cq from Examination cq where cq.id!='7' and cq.ifShow=true  and cq.ifExternal=false  and cq.groupStr like '%" + deptId + "%' order by cq.genTime desc";
        List<Examination> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public List<Examination> findExaminationByLabelAndDept(String labelId,String deptId) {
        String q = "Select cq from Examination cq where cq.id!='7' and cq.ifShow=true and cq.ifExternal=false and cq.labelStr like '%" + labelId + "%' and cq.groupStr like '%" + deptId + "%' order by cq.genTime desc";
        List<Examination> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<Examination> findAllShowedExamination() {
        String q = "Select cq from Examination cq where cq.ifShow=true and cq.id!='7' and cq.ifExternal=false  order by cq.genTime desc";
        List<Examination> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public long getExaminationNum() {
        String q = "Select count(ms) from Examination ms where ms.id !='7' and ms.ifExternal=false ";
        Query qu = this.entityManager.createQuery(q);
        long num = ((Long) (qu.getResultList().get(0)));
        return num;
    }

    @Override
    public Examination findExamination(String id) {
        Examination c = this.entityManager.find(Examination.class, id);
        return c;
    }

    @Override
    public void updateExamination(Examination m) {
        this.entityManager.merge(m);
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<Examination> findExamsByIdList(List<String> idList) {
		List<Examination> es = new LinkedList<>();
		if(idList!=null&&idList.size()>0){
			String q = "Select ms from Examination ms where ms.id in :ids";
			Query qu = this.entityManager.createQuery(q).setParameter("ids", idList);
			es = qu.getResultList();
		}
		return es;
	}
}
