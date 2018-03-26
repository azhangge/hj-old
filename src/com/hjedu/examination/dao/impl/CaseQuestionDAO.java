package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.ICaseQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.CaseQuestion;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.HTMLCleaner;

public class CaseQuestionDAO implements ICaseQuestionDAO, Serializable {
    
    @PersistenceContext
    private EntityManager entityManager;
    IWrongQuestionDAO wqDAO;
    
    public IWrongQuestionDAO getWqDAO() {
        return wqDAO;
    }
    
    public void setWqDAO(IWrongQuestionDAO wqDAO) {
        this.wqDAO = wqDAO;
    }
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    public void addCaseQuestion(CaseQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        this.entityManager.persist(m);
    }
    
    /**
     * 删除某综合题及其对应的一切
     * @param id
     * @param pre 前辍 以使各种成绩共用
     */
    private void deleteRelatedExamCaseItemsByCaseQuestion(String id,String pre) {
        String q01 = "delete from "+pre+"ItemChoice cc where cc.caseItem.question.id='" + id + "'";
        this.entityManager.createQuery(q01).executeUpdate();
        String q02 = "delete from "+pre+"ItemMultiChoice cc where cc.caseItem.question.id='" + id + "'";
        this.entityManager.createQuery(q02).executeUpdate();
        String q03 = "delete from "+pre+"ItemFill cc where cc.caseItem.question.id='" + id + "'";
        this.entityManager.createQuery(q03).executeUpdate();
        String q04 = "delete from "+pre+"ItemJudge cc where cc.caseItem.question.id='" + id + "'";
        this.entityManager.createQuery(q04).executeUpdate();
        String q05 = "delete from "+pre+"ItemEssay cc where cc.caseItem.question.id='" + id + "'";
        this.entityManager.createQuery(q05).executeUpdate();
        String q1 = "delete from "+pre+"ItemCase cc where cc.question.id='" + id + "'";
        this.entityManager.createQuery(q1).executeUpdate();
    }
    /**
     * 删除某综合题及其对应的一切
     * @param id 
     */
    private void deleteRelatedExamCaseItemsByCaseQuestion(String id) {
        this.deleteRelatedExamCaseItemsByCaseQuestion(id, "ExamCase");//删除综合考试条目
        this.deleteRelatedExamCaseItemsByCaseQuestion(id, "BuffetExamCase");//删除综合考试条目
        this.deleteRelatedExamCaseItemsByCaseQuestion(id, "ModuleExamCase");//删除综合考试条目
        this.deleteRelatedExamCaseItemsByCaseQuestion(id, "ContestCase");//删除综合考试条目
    }
    
    @Override
    public void deleteCaseQuestion(String id) {
        this.deleteRelatedExamCaseItemsByCaseQuestion(id);//调用前一方法
        String q5 = "delete from ManualPartItem cc where cc.questionId='" + id + "'";
        
        this.entityManager.createQuery(q5).executeUpdate();
        
        CaseQuestion c = this.entityManager.find(CaseQuestion.class, id);
        this.entityManager.remove(c);
        this.wqDAO.deleteWrongQuestionByQuestion(id);
    }
    
    @Override
    public List<CaseQuestion> findAllCaseQuestion() {
        String q = "Select cq from CaseQuestion cq order by cq.genTime desc";
        List<CaseQuestion> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public CaseQuestion findCaseQuestion(String id) {
        CaseQuestion c = this.entityManager.find(CaseQuestion.class, id);
        return c;
    }
    
    @Override
    public List<CaseQuestion> findCaseQuestionByModule(String id) {
        String q = "Select cq from CaseQuestion cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<CaseQuestion> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public CaseQuestion findCaseQuestionByName(String id) {
        String q = "Select cq from CaseQuestion cq where cq.name=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<CaseQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }
    
    @Override
    public void updateCaseQuestion(CaseQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        this.entityManager.merge(m);
    }
    
    public long getQuestionNumByModule(String id) {
        String q = "Select count(ms) from CaseQuestion ms where ms.module.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }
    
    public CaseQuestion findQuestionByIndex(int index, String mid) {
        String q = "Select cq from CaseQuestion cq where cq.module.id=:id order by cq.id desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", mid);
        qu.setFirstResult(index);
        qu.setMaxResults(1);
        CaseQuestion cqs = (CaseQuestion) qu.getResultList().get(0);
        return cqs;
    }
    
    @Override
    public List<CaseQuestion> findQuestionByLike(String str,String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q1 = "Select cq from CaseQuestion cq where cq.businessId=:businessId and cq.cleanName like :str and cq.module in :modules";
        Query qu = this.entityManager.createQuery(q1);
        qu.setParameter("str", "%" + str + "%");
        qu.setParameter("modules", ai.getModules());
        qu.setParameter("businessId", businessId);
        List<CaseQuestion> cqs = qu.getResultList();
        return cqs;
    }
    
    public List<CaseQuestion> getRandomQuestionOfNumInModule(long num, String mid) {
        List<CaseQuestion> cqs = new ArrayList();
        if (num == 0) {
            return cqs;
        }
        long total = this.getQuestionNumByModule(mid);
        long n = num > total ? total : num;
        if (total == n) {
            List temp = this.findCaseQuestionByModule(mid);
            Collections.shuffle(temp);
            return temp;
        }
        Set<Long> set = new HashSet();
        while (set.size() < n) {
            long tem = (long) (total * Math.random());
            set.add(tem);
        }
        for (long l : set) {
            CaseQuestion cq = this.findQuestionByIndex((int) l, mid);
            cqs.add(cq);
        }
        return cqs;
    }
    
    @Override
    public void deleteCaseQuestionByModule(String moduleId) {
        List<CaseQuestion> jqs = this.findCaseQuestionByModule(moduleId);
        for (CaseQuestion j : jqs) {
            this.deleteCaseQuestion(j.getId());
        }
    }
}
