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
import com.hjedu.examination.dao.GeneralQuestionDAO;
import com.hjedu.examination.dao.IMultiChoiceQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.examination.entity.MultiChoiceQuestion;
import com.hjedu.platform.service.impl.HashCodeService;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.SpringHelper;

public class MultiChoiceQuestionDAO implements IMultiChoiceQuestionDAO, Serializable {

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

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public HashCodeService getHashCodeService() {
        return hashCodeService;
    }

    public void setHashCodeService(HashCodeService hashCodeService) {
        this.hashCodeService = hashCodeService;
    }

    @Override
    public void addMultiChoiceQuestion(MultiChoiceQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.persist(m);
        GeneralQuestionDAO gqdao = SpringHelper.getSpringBean("GeneralQuestionDAO");
        gqdao.syncGeneralQuestion(m);
    }

    @Override
    public void deleteMultiChoiceQuestion(String id) {
        String q1 = "delete from ExamCaseItemMultiChoice cc where cc.question.id='" + id + "'";
        String q2 = "delete from BuffetExamCaseItemMultiChoice cc where cc.question.id='" + id + "'";
        String q3 = "delete from ModuleExamCaseItemMultiChoice cc where cc.question.id='" + id + "'";
        String q4 = "delete from ContestCaseItemMultiChoice cc where cc.question.id='" + id + "'";
        String q5 = "delete from ManualPartItem cc where cc.questionId='" + id + "'";

        this.entityManager.createQuery(q1).executeUpdate();
        this.entityManager.createQuery(q2).executeUpdate();
        this.entityManager.createQuery(q3).executeUpdate();
        this.entityManager.createQuery(q4).executeUpdate();
        this.entityManager.createQuery(q5).executeUpdate();

        MultiChoiceQuestion c = this.entityManager.find(MultiChoiceQuestion.class, id);
        if (c != null) {
            this.entityManager.remove(c);
            this.wqDAO.deleteWrongQuestionByQuestion(id);
        }
        GeneralQuestionDAO gqdao = SpringHelper.getSpringBean("GeneralQuestionDAO");
        gqdao.delete(id);
    }

    @Override
    public List<MultiChoiceQuestion> findAllMultiChoiceQuestion() {
        String q = "Select cq from MultiChoiceQuestion cq order by cq.genTime desc";
        List<MultiChoiceQuestion> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public MultiChoiceQuestion findMultiChoiceQuestion(String id) {
        MultiChoiceQuestion c = this.entityManager.find(MultiChoiceQuestion.class, id);
        return c;
    }

    @Override
    public List<MultiChoiceQuestion> findMultiChoiceQuestionByModule(String id) {
        String q = "Select cq from MultiChoiceQuestion cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        List<MultiChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<MultiChoiceQuestion> findMultiChoiceQuestionByModuleIds(List<String> ids) {
        String q = "Select cq from MultiChoiceQuestion cq where cq.module.id in :ids";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("ids", ids);
        List<MultiChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<MultiChoiceQuestion> findMultiChoiceQuestionByModule(String id, int offSet, int num) {
        String q = "Select cq from MultiChoiceQuestion cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<MultiChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<MultiChoiceQuestion> findOrderedMultiChoiceQuestionByModule(String id, int offSet, int num, String field, String type) {
        String q = "Select cq from MultiChoiceQuestion cq where cq.module.id=:id order by cq." + field + " " + type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<MultiChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<MultiChoiceQuestion> findMultiChoiceQuestionByModuleAndLike(String id, Map<String, Object> fms) {
        String q1 = "Select cq from MultiChoiceQuestion cq where cq.module.id=:id";
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
        List<MultiChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateMultiChoiceQuestion(MultiChoiceQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.merge(m);
        GeneralQuestionDAO gqdao = SpringHelper.getSpringBean("GeneralQuestionDAO");
        gqdao.syncGeneralQuestion(m);
    }

    public long getQuestionNumByModule(String id) {
        String q = "Select count(ms) from MultiChoiceQuestion ms where ms.module.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) qu.getResultList().get(0)).longValue();
        return num;
    }

    @Override
    public void deleteMultiChoiceQuestionByModule(String moduleId) {
        List<MultiChoiceQuestion> jqs = this.findMultiChoiceQuestionByModule(moduleId);
        for (MultiChoiceQuestion j : jqs) {
            this.deleteMultiChoiceQuestion(j.getId());
        }
    }

    @Override
    public MultiChoiceQuestion findMultiChoiceQuestionByName(String id) {
        String q = "Select cq from MultiChoiceQuestion cq where cq.name=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<MultiChoiceQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    @Override
    public MultiChoiceQuestion findMultiChoiceQuestionByHashCode(String id) {
        String q = "Select cq from MultiChoiceQuestion cq where cq.hashCode=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<MultiChoiceQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    public MultiChoiceQuestion findQuestionByIndex(int index, String mid) {
        String q = "Select cq from MultiChoiceQuestion cq where cq.module.id=:id order by cq.id desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", mid);
        qu.setFirstResult(index);
        qu.setMaxResults(1);
        MultiChoiceQuestion cqs = (MultiChoiceQuestion) qu.getResultList().get(0);
        return cqs;
    }

    @Override
    public List<MultiChoiceQuestion> findQuestionByLike(String str,String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q1 = "Select cq from MultiChoiceQuestion cq where cq.businessId=:businessId and cq.cleanName like :str and cq.module in :modules";
        Query qu = this.entityManager.createQuery(q1);
        qu.setParameter("str", "%" + str + "%");
        qu.setParameter("modules", ai.getModules());
        qu.setParameter("businessId", businessId);
        List<MultiChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    public List<MultiChoiceQuestion> getRandomQuestionOfNumInModule(long num, String mid) {
        List<MultiChoiceQuestion> cqs = new ArrayList();
        if (num == 0) {
            return cqs;
        }
        long total = this.getQuestionNumByModule(mid);
        long n = num > total ? total : num;
        if (total == n) {
            List temp = this.findMultiChoiceQuestionByModule(mid);
            Collections.shuffle(temp);
            return temp;
        }
        Set<Long> set = new HashSet();
        while (set.size() < n) {
            long tem = (long) (total * Math.random());
            set.add(tem);
        }
        for (long l : set) {
            MultiChoiceQuestion cq = this.findQuestionByIndex((int) l, mid);
            cqs.add(cq);
        }
        return cqs;
    }

    @Override
    public List<MultiChoiceQuestion> findWrongQuestion() {
        Set set = new HashSet();
        AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q1 = "select cq from MultiChoiceQuestion cq where cq.answer='' or cq.answer is null and cq.module in :modules";
        String q2 = "select cq from MultiChoiceQuestion cq where cq.id not in (select ch.question.id from ExamMultiChoice ch) and cq.module in :modules";
        List list = new ArrayList();
        if(ai==null||ai.getModules()==null){
        	return list;
        }
        if(ai.getModules().size()>=1){
	        List<MultiChoiceQuestion> list1 = this.entityManager.createQuery(q1).setParameter("modules", ai.getModules()).getResultList();
	        for (GeneralQuestion cq : list1) {
	            cq.setWrongStr("无答案");
	        }
	        set.addAll(list1);
	        List<MultiChoiceQuestion> list2 = this.entityManager.createQuery(q2).setParameter("modules", ai.getModules()).getResultList();
	        for (GeneralQuestion cq : list2) {
	            String str = cq.getWrongStr();
	            if (str == null || "".equals(str)) {
	                str = "无选项";
	            } else {
	                str = str + ",无选项";
	            }
	            cq.setWrongStr(str);
	        }
	        set.addAll(list2);
        }
        list.addAll(set);
        return list;
    }

    public static void main(String args[]) {
        IMultiChoiceQuestionDAO dao = SpringHelper.getSpringBean("MultiChoiceQuestionDAO");
        List<MultiChoiceQuestion> cqs = dao.findWrongQuestion();
        System.out.println(cqs.size());
    }

}
