package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IBuffetExamCaseDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.buffet.BuffetExamCase;
import com.hjedu.examination.entity.buffet.BuffetExamCaseItemFile;

public class BuffetExamCaseDAO implements IBuffetExamCaseDAO, Serializable {
    
    @PersistenceContext
    private EntityManager entityManager;
    IFileQuestionDAO fqDAO;
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public IFileQuestionDAO getFqDAO() {
        return fqDAO;
    }
    
    public void setFqDAO(IFileQuestionDAO fqDAO) {
        this.fqDAO = fqDAO;
    }
    
    @Override
    public void addBuffetExamCase(BuffetExamCase m) {
        this.entityManager.persist(m);
    }
    
    @Override
    public void deleteBuffetExamCase(String id) {
        BuffetExamCase c = this.entityManager.find(BuffetExamCase.class, id);
        List<BuffetExamCaseItemFile> files = c.getFileItems();
        for (BuffetExamCaseItemFile f : files) {
            this.fqDAO.delFile(f.getId());
        }
        this.entityManager.remove(c);
    }
    
    @Override
    public List<BuffetExamCase> findAllBuffetExamCase(String businessId) {
        String q = "Select cq from BuffetExamCase cq where cq.businessId=:businessId and cq.examination.id!='7' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    
    @Override
    public List<BuffetExamCase> findAllSubmittedBuffetExamCase() {
        String q = "Select cq from BuffetExamCase cq where cq.examination.id!='7' and cq.stat='submitted' order by cq.genTime desc";
        List<BuffetExamCase> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public BuffetExamCase findBuffetExamCase(String id) {
        BuffetExamCase c = this.entityManager.find(BuffetExamCase.class, id);
        return c;
    }
    
    @Override
    public List<BuffetExamCase> findBuffetExamCaseByExamination(String id) {
        String q = "Select cq from BuffetExamCase cq where cq.examination.id=:qid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<BuffetExamCase> findSubmittedBuffetExamCaseByExamination(String id) {
        String q = "Select cq from BuffetExamCase cq where cq.examination.id=:qid and cq.stat='submitted' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<BuffetExamCase> findBuffetExamCaseByExaminationAndUser(String questionId,String uid) {
        String q = "Select cq from BuffetExamCase cq where cq.examination.id=:qid and cq.user.id=:uid order by cq.genTime desc";
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", questionId);
        qu.setParameter("uid", uid);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<BuffetExamCase> findBuffetExamCaseByUser(String userId) {
        String q = "Select cq from BuffetExamCase cq where cq.user.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public long getBuffetExamCaseNum() {
        String q = "Select count(ms) from BuffetExamCase ms where ms.examination.id !='7'";
        Query qu = this.entityManager.createQuery(q);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }
    
    @Override
    public List<BuffetExamCase> findLotsOfBuffetExamCase(int offSet, int num) {
        String q = "Select cq from BuffetExamCase cq order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<BuffetExamCase> findOrderedBuffetExamCase(int offSet, int num,String field,String type) {
        String q = "Select cq from BuffetExamCase cq order by cq."+field+" "+type;
        Query qu = this.entityManager.createQuery(q);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<BuffetExamCase> findBuffetExamCaseByLike( Map<String, String> fms) {
        String q1 = "Select cq from BuffetExamCase cq where 1=1 ";
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty);
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q1 + q2);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public long getBuffetExamCaseNumByAdmin(AdminInfo admin) {
        String q = "Select count(ms) from BuffetExamCase ms where :admin member of ms.examination.admins";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("admin", admin);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }
    @Override
    public List<BuffetExamCase> findLotsOfBuffetExamCaseByAdmin(AdminInfo admin,int offSet, int num) {
        String q = "Select cq from BuffetExamCase cq where :admin member of cq.examination.admins order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("admin", admin);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<BuffetExamCase> findOrderedBuffetExamCaseByAdmin(AdminInfo admin,int offSet, int num,String field,String type) {
        String q = "Select cq from BuffetExamCase cq where :admin member of cq.examination.admins order by cq."+field+" "+type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("admin", admin);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<BuffetExamCase> findBuffetExamCaseByLikeAndAdmin(AdminInfo admin, Map<String, String> fms) {
        String q1 = "Select cq from BuffetExamCase cq where :admin member of cq.examination.admins ";
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty);
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q1 + q2);
        qu.setParameter("admin", admin);
        List<BuffetExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    
    @Override
    public void updateBuffetExamCase(BuffetExamCase m) {
        this.entityManager.merge(m);
    }
    
    @Override
    public long getParticipateNumByExamAndUser(String examId,String uid) {
        String q = "Select count(ms) from BuffetExamCase ms where ms.examination.id=:examId and ms.user.id=:uid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        qu.setParameter("uid", uid);
        long num = ((Long)( qu.getResultList().get(0))).longValue();
        return num;
    }
    
    public void deleteAllBuffetExamCase() {
        
        //删除所有成绩条目
        String q1 = "delete from b_exam_case_item_choice";
        this.entityManager.createNativeQuery(q1).executeUpdate();
        String q2 = "delete from b_exam_case_item_multi_choice";
        this.entityManager.createNativeQuery(q2).executeUpdate();
        String q3 = "delete from b_exam_case_item_fill";
        this.entityManager.createNativeQuery(q3).executeUpdate();
        String q4 = "delete from b_exam_case_item_judge";
        this.entityManager.createNativeQuery(q4).executeUpdate();
        String q5 = "delete from b_exam_case_item_essay";
        this.entityManager.createNativeQuery(q5).executeUpdate();
        String q6 = "delete from b_exam_case_item_file";
        this.entityManager.createNativeQuery(q6).executeUpdate();
        String q7 = "delete from b_exam_case_item_case";
        this.entityManager.createNativeQuery(q7).executeUpdate();
        
        
        String q = "delete from BuffetExamCase cq";
         this.entityManager.createQuery(q).executeUpdate();
    }
    
}
