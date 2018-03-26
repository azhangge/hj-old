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
import com.hjedu.examination.dao.IFillQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.FillQuestion;
import com.hjedu.platform.service.impl.HashCodeService;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.HTMLCleaner;

public class FillQuestionDAO implements IFillQuestionDAO, Serializable {

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
    public void addFillQuestion(FillQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.persist(m);
    }

    @Override
    public void deleteFillQuestion(String id) {
        String q1 = "delete from ExamCaseItemFill cc where cc.question.id='" + id + "'";
        String q2 = "delete from BuffetExamCaseItemFill cc where cc.question.id='" + id + "'";
        String q3 = "delete from ModuleExamCaseItemFill cc where cc.question.id='" + id + "'";
        String q4 = "delete from ContestCaseItemFill cc where cc.question.id='" + id + "'";
        String q5 = "delete from ManualPartItem cc where cc.questionId='" + id + "'";
        
        this.entityManager.createQuery(q1).executeUpdate();
        this.entityManager.createQuery(q2).executeUpdate();
        this.entityManager.createQuery(q3).executeUpdate();
        this.entityManager.createQuery(q4).executeUpdate();
        this.entityManager.createQuery(q5).executeUpdate();
        
        FillQuestion c = this.entityManager.find(FillQuestion.class, id);
        if (c != null) {
            this.entityManager.remove(c);
            this.wqDAO.deleteWrongQuestionByQuestion(id);
        }
        
    }

    @Override
    public List<FillQuestion> findAllFillQuestion() {
        String q = "Select cq from FillQuestion cq order by cq.genTime desc";
        List<FillQuestion> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public FillQuestion findFillQuestion(String id) {
        FillQuestion c = this.entityManager.find(FillQuestion.class, id);
        return c;
    }
    
    
    @Override
    public List<FillQuestion> findFillQuestionByModule(String id, int offSet, int num) {
        String q = "Select cq from FillQuestion cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<FillQuestion> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<FillQuestion> findOrderedFillQuestionByModule(String id, int offSet, int num,String field,String type) {
        String q = "Select cq from FillQuestion cq where cq.module.id=:id order by cq."+field+" "+type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<FillQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<FillQuestion> findFillQuestionByModuleAndLike(String id,  Map<String, Object> fms) {
        String q1 = "Select cq from FillQuestion cq where cq.module.id=:id";
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
        List<FillQuestion> cqs = qu.getResultList();
        return cqs;
    }


    @Override
    public List<FillQuestion> findFillQuestionByModule(String id) {
        String q = "Select cq from FillQuestion cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<FillQuestion> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<FillQuestion> findFillQuestionByModuleIds(List<String> ids) {
        String q = "Select cq from FillQuestion cq where cq.module.id in :ids";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("ids", ids);
        List<FillQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateFillQuestion(FillQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.merge(m);
    }

    public long getQuestionNumByModule(String id) {
        String q = "Select count(ms) from FillQuestion ms where ms.module.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }
    
    @Override
    public List<FillQuestion> findQuestionByLike(String str,String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q1 = "Select cq from FillQuestion cq where cq.businessId=:businessId and cq.cleanName like :str and cq.module in :modules";
        Query qu = this.entityManager.createQuery(q1);
        qu.setParameter("str", "%"+str+"%");
        qu.setParameter("modules", ai.getModules());
        qu.setParameter("businessId", businessId);
        List<FillQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void deleteFillQuestionByModule(String moduleId) {
        List<FillQuestion> jqs = this.findFillQuestionByModule(moduleId);
        for (FillQuestion j : jqs) {
            this.deleteFillQuestion(j.getId());
        }
    }

    @Override
    public FillQuestion findFillQuestionByName(String id) {
        String q = "Select cq from FillQuestion cq where cq.name=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<FillQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }
    
    @Override
    public FillQuestion findFillQuestionByHashCode(String id) {
        String q = "Select cq from FillQuestion cq where cq.hashCode=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<FillQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    public FillQuestion findQuestionByIndex(int index, String mid) {
        String q = "Select cq from FillQuestion cq where cq.module.id=:id order by cq.id desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", mid);
        qu.setFirstResult(index);
        qu.setMaxResults(1);
        FillQuestion cqs = (FillQuestion) qu.getResultList().get(0);
        return cqs;
    }

    public List<FillQuestion> getRandomQuestionOfNumInModule(long num, String mid) {
        List<FillQuestion> cqs = new ArrayList();
        if (num == 0) {
            return cqs;
        }
        long total = this.getQuestionNumByModule(mid);
        long n = num > total ? total : num;
        if (total == n) {
            List temp = this.findFillQuestionByModule(mid);
            Collections.shuffle(temp);
            return temp;
        }
        Set<Long> set = new HashSet();
        while (set.size() < n) {
            long tem = (long) (total * Math.random());
            set.add(tem);
        }
        for (long l : set) {
            FillQuestion cq = this.findQuestionByIndex((int) l, mid);
            cqs.add(cq);
        }
        return cqs;
    }
}
