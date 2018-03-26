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
import com.hjedu.examination.dao.IChoiceQuestionDAO;
import com.hjedu.examination.dao.IWrongQuestionDAO;
import com.hjedu.examination.entity.ChoiceQuestion;
import com.hjedu.examination.entity.ExamModuleModel;
import com.hjedu.examination.entity.GeneralQuestion;
import com.hjedu.platform.service.impl.HashCodeService;
import com.huajie.util.ExternalUserUtil;
import com.huajie.util.HTMLCleaner;
import com.huajie.util.RereRandom;
import com.huajie.util.SpringHelper;

public class ChoiceQuestionDAO implements IChoiceQuestionDAO, Serializable {

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
    public void addChoiceQuestion(ChoiceQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.persist(m);
        GeneralQuestionDAO gqdao = SpringHelper.getSpringBean("GeneralQuestionDAO");
        gqdao.syncGeneralQuestion(m);
    }

    @Override
    public void deleteChoiceQuestion(String id) {
        String q1 = "delete from ExamCaseItemChoice cc where cc.question.id='" + id + "'";
        String q2 = "delete from BuffetExamCaseItemChoice cc where cc.question.id='" + id + "'";
        String q3 = "delete from ModuleExamCaseItemChoice cc where cc.question.id='" + id + "'";
        String q4 = "delete from ContestCaseItemChoice cc where cc.question.id='" + id + "'";
        String q5 = "delete from ManualPartItem cc where cc.questionId='" + id + "'";

        this.entityManager.createQuery(q1).executeUpdate();
        this.entityManager.createQuery(q2).executeUpdate();
        this.entityManager.createQuery(q3).executeUpdate();
        this.entityManager.createQuery(q4).executeUpdate();
        this.entityManager.createQuery(q5).executeUpdate();

        ChoiceQuestion c = this.entityManager.find(ChoiceQuestion.class, id);
        if (c != null) {
            this.entityManager.remove(c);
            this.wqDAO.deleteWrongQuestionByQuestion(id);
        }
        GeneralQuestionDAO gqdao = SpringHelper.getSpringBean("GeneralQuestionDAO");
        gqdao.delete(id);
    }

    @Override
    public List<ChoiceQuestion> findAllChoiceQuestion() {
        String q = "Select cq from ChoiceQuestion cq order by cq.genTime desc";
        List<ChoiceQuestion> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public ChoiceQuestion findChoiceQuestion(String id) {
        ChoiceQuestion c = this.entityManager.find(ChoiceQuestion.class, id);
        return c;
    }

    @Override
    public List<ChoiceQuestion> findChoiceQuestionByModule(String id) {
        //String q = "Select cq from ChoiceQuestion cq where cq.module.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createNamedQuery("findChoiceQuestionByModule");
        qu.setParameter("id", id);
        List<ChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ChoiceQuestion> findChoiceQuestionByModuleIds(List<String> ids) {
        String q = "Select cq from ChoiceQuestion cq where cq.module.id in :ids";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("ids", ids);
        List<ChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ChoiceQuestion> findChoiceQuestionByModule(String id, int offSet, int num) {
        String q = "Select cq from ChoiceQuestion cq where cq.module.id=:id order by cq.genTime desc,cq.ord";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ChoiceQuestion> findOrderedChoiceQuestionByModule(String id, int offSet, int num, String field, String type) {
        String q = "Select cq from ChoiceQuestion cq where cq.module.id=:id order by cq." + field + " " + type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ChoiceQuestion> findChoiceQuestionByModuleAndLike(String id, Map<String, Object> fms) {
        String q1 = "Select cq from ChoiceQuestion cq where cq.module.id=:id";
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
        List<ChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ChoiceQuestion> findQuestionByLike(String str,String businessId) {
    	AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q1 = "Select cq from ChoiceQuestion cq where cq.businessId=:businessId and cq.cleanName like :str and cq.module in :modules";
        Query qu = this.entityManager.createQuery(q1);
        qu.setParameter("str", "%" + str + "%");
        qu.setParameter("modules", ai.getModules());
        qu.setParameter("businessId", businessId);
        List<ChoiceQuestion> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public void updateChoiceQuestion(ChoiceQuestion m) {
        m.setCleanName(HTMLCleaner.delHTMLTag(m.getName()));
        m.setHashCode(this.hashCodeService.fetchHashCode(m));
        this.entityManager.merge(m);
        GeneralQuestionDAO gqdao = SpringHelper.getSpringBean("GeneralQuestionDAO");
        gqdao.syncGeneralQuestion(m);
    }

    @Override
    public long getQuestionNumByModule(String id) {
        String q = "Select count(ms) from ChoiceQuestion ms where ms.module.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public ChoiceQuestion findQuestionByIndex(int index, String mid) {
        String q = "Select cq from ChoiceQuestion cq where cq.module.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", mid);
        qu.setFirstResult(index);
        qu.setMaxResults(1);

        ChoiceQuestion cqs = (ChoiceQuestion) qu.getResultList().get(0);
        return cqs;
    }

    /**
     * 以批量方式从模块中抽取试题 理论原理为：先取出本模块所有ID，然后根据SET中的随机数，取得要抽取的试题ID列表，然后以批量方式从模块中取这些试题
     *
     * @param set 随机数集
     * @param mid 模块id
     * @return
     */
    public List<ChoiceQuestion> findQuestionsByIndexSet(Set<Long> set, String mid) {
        String q1 = "Select cq.id from ChoiceQuestion cq where cq.module.id=:id";
        Query qu1 = this.entityManager.createQuery(q1);
        qu1.setParameter("id", mid);
        List<String> result = qu1.getResultList();//取得本模块的所有ID
        List<String> list = new ArrayList();//保存要抽取的ID列表
        for (Long l : set) {
            try {
                String idd = result.get(l.intValue());
                list.add(idd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //以批量方式抽取试题，理论上应该比单条抽取快得多
        String q2 = "Select cq from ChoiceQuestion cq where cq.module.id=:id and cq.id in :list";
        Query qu2 = this.entityManager.createQuery(q2);
        qu2.setParameter("id", mid);
        qu2.setParameter("list", list);
        List<ChoiceQuestion> cqs = qu2.getResultList();
        return cqs;
    }

    @Override
    public ChoiceQuestion findChoiceQuestionByName(String id) {
        String q = "Select cq from ChoiceQuestion cq where cq.name=:name order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("name", id);
        List<ChoiceQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    @Override
    public ChoiceQuestion findChoiceQuestionByHashCode(String code) {
        String q = "Select cq from ChoiceQuestion cq where cq.hashCode=:code order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("code", code);
        List<ChoiceQuestion> cqs = qu.getResultList();
        if (cqs.isEmpty()) {
            return null;
        } else {
            return cqs.get(0);
        }
    }

    @Override
    public List<ChoiceQuestion> getRandomQuestionOfNumInModule(long num, String mid, List<ChoiceQuestion> cqs2) {
        List<ChoiceQuestion> cqs = new ArrayList();
        if (num == 0) {
            return cqs;
        }
        long total = this.getQuestionNumByModule(mid);
        long n = num > total ? total : num;
        if (total == n) {
            List temp = this.findChoiceQuestionByModule(mid);
            return temp;
        }
        Set<Long> set = new HashSet();
        while (true) {
            long tem = (long) (total * Math.random());
            if (!set.contains(tem)) {
                set.add(tem);
                ChoiceQuestion cq = this.findQuestionByIndex((int) tem, mid);
                if (cqs2.contains(cq)) {
                    total--;
                    n = num > total ? total : num;
                } else {
                    cqs.add(cq);
                }
            }
            if (cqs.size() >= n) {
                break;
            }

        }

        return cqs;
    }

    @Override
    public List<ChoiceQuestion> getRandomQuestionOfNumInModule(long num, String mid) {
        List<ChoiceQuestion> cqs = new ArrayList();
        if (num == 0) {
            return cqs;
        }
        long total = this.getQuestionNumByModule(mid);
        long n = num > total ? total : num;
        if (total == n) {
            List temp = this.findChoiceQuestionByModule(mid);
            Collections.shuffle(temp);
            return temp;
        }
//        Set<Long> set = new HashSet();
//        while (set.size() < n) {
//            long tem = (long) (total * Math.random());
//            set.add(tem);
//        }
        Set<Long> set = RereRandom.randomSet(0, total, num);
//        for (long l : set) {
//            ChoiceQuestion cq = this.findQuestionByIndex((int) l, mid);
//            cqs.add(cq);
//        }
        cqs = this.findQuestionsByIndexSet(set, mid);
        return cqs;
    }

    @Override
    public void deleteChoiceQuestionByModule(String moduleId) {
        List<ChoiceQuestion> jqs = this.findChoiceQuestionByModule(moduleId);
        for (ChoiceQuestion j : jqs) {
            this.deleteChoiceQuestion(j.getId());
        }
    }

    @Override
    public List<ChoiceQuestion> findWrongQuestion() {
        Set set = new HashSet();
        AdminInfo ai = ExternalUserUtil.getAdminBySession();
        String q1 = "select cq from ChoiceQuestion cq where cq.answer='' or cq.answer is null and cq.module in :modules";
        String q2 = "select cq from ChoiceQuestion cq where cq.id not in (select ch.question.id from ExamChoice ch) and cq.module in :modules";
        List list = new ArrayList();
        if(ai==null||ai.getModules()==null){
        	return list;
        }
        if(ai.getModules().size()>=1){
        	List<ChoiceQuestion> list1 = this.entityManager.createQuery(q1).setParameter("modules", ai.getModules()).getResultList();
            for (GeneralQuestion cq : list1) {
                cq.setWrongStr("无答案");
            }
            set.addAll(list1);
            
            List<ChoiceQuestion> list2 = this.entityManager.createQuery(q2).setParameter("modules", ai.getModules()).getResultList();
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
        IChoiceQuestionDAO dao = SpringHelper.getSpringBean("ChoiceQuestionDAO");
        List<ChoiceQuestion> cqs = dao.findWrongQuestion();
        System.out.println(cqs.size());
    }

}
