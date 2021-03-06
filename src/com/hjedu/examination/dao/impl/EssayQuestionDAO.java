package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IEssayQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.EssayQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.platform.service.impl.HashCodeService;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.HTMLCleaner;

public class EssayQuestionDAO implements IEssayQuestionDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    IWrongQuestionDAO wqDAO;
    HashCodeService hashCodeService;

    public IWrongQuestionDAO getWqDAO() {
        return wqDAO;
    }

    public void setWqDAO(IWrongQuestionDAO wqDAO) {
        this.wqDAO = wqDAO;
    }

    public HashCodeService getHashCodeService() {
        return hashCodeService;
    }

    public void setHashCodeService(HashCodeService hashCodeService) {
        this.hashCodeService = hashCodeService;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void addEssayQuestion(EssayQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.persist(m);
    }

    @Override
    public void deleteEssayQuestion(String id) {
        String q1 = "delete from ExamCaseItemEssay cc where cc.question.id='" + id + "'";
        String q2 = "delete from BuffetExamCaseItemEssay cc where cc.question.id='" + id + "'";
        String q3 = "delete from ModuleExamCaseItemEssay cc where cc.question.id='" + id + "'";
        String q4 = "delete from ContestCaseItemEssay cc where cc.question.id='" + id + "'";
        String q5 = "delete from ManualPartItem cc where cc.questionId='" + id + "'";
        
        this.entityManager.createQuery(q1).executeUpdate();
        this.entityManager.createQuery(q2).executeUpdate();
        this.entityManager.createQuery(q3).executeUpdate();
        this.entityManager.createQuery(q4).executeUpdate();
        this.entityManager.createQuery(q5).executeUpdate();
        
        EssayQuestion c = this.entityManager.find(EssayQuestion.class, id);
        if (c != null) {
            this.entityManager.remove(c);
            this.wqDAO.deleteWrongQuestionByQuestion(id);
        }
    }

    @Override
    public List<EssayQuestion> findAllEssayQuestion() {
        String q = "Select cq from EssayQuestion cq order by cq.genTime desc";
        List<EssayQuestion> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public EssayQuestion findEssayQuestion(String id) {
        EssayQuestion c = this.entityManager.find(EssayQuestion.class, id);
        return c;
    }

    @Override
    public List<EssayQuestion> findEssayQuestionByModule(String id) {
        String q = "Select cq from EssayQuestion cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<EssayQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateEssayQuestion(EssayQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.merge(m);
    }

    public long getQuestionNumByModule(String id) {
        String q = "Select count(ms) from EssayQuestion ms where ms.module.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }
    
    @Override
    public List<EssayQuestion> findQuestionByLike(String str,String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q1 = "Select cq from EssayQuestion cq where cq.businessId=:businessId and cq.cleanName like :str and cq.module in :modules";
        Query qu = this.entityManager.createQuery(q1);
        qu.setParameter("str", "%"+str+"%");
        qu.setParameter("modules", ai.getModules());
        qu.setParameter("businessId", businessId);
        List<EssayQuestion> cqs = qu.getResultList();
        return cqs;
    }
    
    
    @Override
    public List<EssayQuestion> findEssayQuestionByModule(String id, int offSet, int num) {
        String q = "Select cq from EssayQuestion cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<EssayQuestion> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<EssayQuestion> findOrderedEssayQuestionByModule(String id, int offSet, int num,String field,String type) {
        String q = "Select cq from EssayQuestion cq where cq.module.id=:id order by cq."+field+" "+type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<EssayQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<EssayQuestion> findEssayQuestionByModuleAndLike(String id,  Map<String, Object> fms) {
        String q1 = "Select cq from EssayQuestion cq where cq.module.id=:id";
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {

            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }

        Query qu = this.entityManager.createQuery(q1 + q2);
        qu.setParameter("id", id);
        List<EssayQuestion> cqs = qu.getResultList();
        return cqs;
    }


    @Override
    public void deleteEssayQuestionByModule(String moduleId) {
        List<EssayQuestion> jqs = this.findEssayQuestionByModule(moduleId);
        for (EssayQuestion j : jqs) {
            this.deleteEssayQuestion(j.getId());
        }
    }

    public EssayQuestion findQuestionByIndex(int index, String mid) {
        String q = "Select cq from EssayQuestion cq where cq.module.id=:id order by cq.id desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", mid);
        qu.setFirstResult(index);
        qu.setMaxResults(1);
        EssayQuestion cqs = (EssayQuestion) qu.getResultList().get(0);
        return cqs;
    }

    @Override
    public EssayQuestion findEssayQuestionByName(String id) {
        String q = "Select cq from EssayQuestion cq where cq.name=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<EssayQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }
    
    @Override
    public EssayQuestion findEssayQuestionByHashCode(String id) {
        String q = "Select cq from EssayQuestion cq where cq.hashCode=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<EssayQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    public List<EssayQuestion> getRandomQuestionOfNumInModule(long num, String mid) {
        List<EssayQuestion> cqs = new ArrayList();
        if (num == 0) {
            return cqs;
        }
        long total = this.getQuestionNumByModule(mid);
        long n = num > total ? total : num;
        if (total == n) {
            List temp=this.findEssayQuestionByModule(mid);
            Collections.shuffle(temp);
            return temp;
        }
        Set<Long> set = new HashSet();
        while (set.size() < n) {
            long tem = (long) (total * Math.random());
            set.add(tem);
        }
        for (long l : set) {
            EssayQuestion cq = this.findQuestionByIndex((int) l, mid);
            cqs.add(cq);
        }
        return cqs;
    }
}
