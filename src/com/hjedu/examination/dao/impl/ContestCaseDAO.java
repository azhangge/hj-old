package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ContestCaseItemFile;

public class ContestCaseDAO implements IContestCaseDAO, Serializable {
    
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
    public void addContestCase(ContestCase m) {
        this.entityManager.persist(m);
    }
    
    
    
    @Override
    public List<ContestCase> findAllContestCase() {
        String q = "Select cq from ContestCase cq where cq.examination.id!='7' order by cq.genTime desc";
        List<ContestCase> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    
    @Override
    public List<ContestCase> findAllSubmittedContestCase() {
        String q = "Select cq from ContestCase cq where cq.examination.id!='7' and cq.stat='submitted' order by cq.genTime desc";
        List<ContestCase> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }
    
    @Override
    public ContestCase findContestCase(String id) {
        ContestCase c = this.entityManager.find(ContestCase.class, id);
        return c;
    }
    
    //@Override
    public List<ContestCase> findContestCaseByContest(String id) {
        String q = "Select cq from ContestCase cq where cq.examination.id=:qid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ContestCase> findContestCaseByContestAndSession(String id,String str) {
        String q = "Select cq from ContestCase cq where cq.examination.id=:qid and cq.sessionStr=:str order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        qu.setParameter("str", str);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ContestCase> findSubmittedContestCaseByContest(String id) {
        String q = "Select cq from ContestCase cq where cq.examination.id=:qid and cq.stat='submitted' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ContestCase> findContestCaseByContestAndUser(String questionId,String uid) {
        String q = "Select cq from ContestCase cq where cq.examination.id=:qid and cq.user.id=:uid order by cq.genTime desc";
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", questionId);
        qu.setParameter("uid", uid);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ContestCase> findContestCaseByContestSession(String sid) {
        String q = "Select cq from ContestCase cq where cq.sessionId=:qid order by cq.genTime desc";
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("sid", sid);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ContestCase> findUnAwardedContestCaseByContestSession(String sid) {
        String q = "Select cq from ContestCase cq where cq.sessionId=:sid and cq.ifAwarded=false order by cq.genTime desc";
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("sid", sid);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ContestCase> findContestCaseByUser(String userId) {
        String q = "Select cq from ContestCase cq where cq.user.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public long getContestCaseNum() {
        String q = "Select count(ms) from ContestCase ms where ms.examination.id !='7'";
        Query qu = this.entityManager.createQuery(q);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }
    
    @Override
    public List<ContestCase> findLotsOfContestCase(int offSet, int num) {
        String q = "Select cq from ContestCase cq order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ContestCase> findOrderedContestCase(int offSet, int num,String field,String type) {
        String q = "Select cq from ContestCase cq order by cq."+field+" "+type;
        Query qu = this.entityManager.createQuery(q);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ContestCase> findContestCaseByLike( Map<String, Object> fms) {
        String q1 = "Select cq from ContestCase cq where 1=1 ";
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q1 + q2);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public long getContestCaseNumByAdmin(AdminInfo admin) {
        String q = "Select count(ms) from ContestCase ms where :admin member of ms.examination.admins";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("admin", admin);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }
    @Override
    public List<ContestCase> findLotsOfContestCaseByAdmin(AdminInfo admin,int offSet, int num) {
        String q = "Select cq from ContestCase cq where :admin member of cq.examination.admins order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("admin", admin);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ContestCase> findOrderedContestCaseByAdmin(AdminInfo admin,int offSet, int num,String field,String type) {
        String q = "Select cq from ContestCase cq where :admin member of cq.examination.admins order by cq."+field+" "+type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("admin", admin);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ContestCase> findContestCaseByLikeAndAdmin(AdminInfo admin, Map<String, Object> fms) {
        String q1 = "Select cq from ContestCase cq where :admin member of cq.examination.admins ";
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q1 + q2);
        qu.setParameter("admin", admin);
        List<ContestCase> cqs = qu.getResultList();
        return cqs;
    }
    
    
    @Override
    public void updateContestCase(ContestCase m) {
        this.entityManager.merge(m);
    }
    
    @Override
    public long getParticipateNumByContestAndUser(String examId,String str,String uid) {
        String q = "Select count(ms) from ContestCase ms where ms.examination.id=:examId and ms.sessionStr=:str and ms.user.id=:uid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        qu.setParameter("str", str);
        qu.setParameter("uid", uid);
        long num = ((Long)( qu.getResultList().get(0))).longValue();
        return num;
    }
    
    @Override
    public long getParticipateNumByContest(String examId) {
        String q = "Select count(ms) from ContestCase ms where ms.examination.id=:examId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        long num = ((Long)( qu.getResultList().get(0))).longValue();
        return num;
    }
    
    @Override
    public long getParticipateNumByContestAndSession(String examId,String str) {
        String q = "Select count(ms) from ContestCase ms where ms.examination.id=:examId and ms.sessionStr=:str";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        qu.setParameter("str", str);
        long num = ((Long)( qu.getResultList().get(0))).longValue();
        return num;
    }
    
    @Override
    public void deleteAllContestCase() {
        
        //删除所有成绩条目
        String q1 = "delete from contest_case_item_choice";
        this.entityManager.createNativeQuery(q1).executeUpdate();
        String q2 = "delete from contest_case_item_multi_choice";
        this.entityManager.createNativeQuery(q2).executeUpdate();
        String q3 = "delete from contest_case_item_fill";
        this.entityManager.createNativeQuery(q3).executeUpdate();
        String q4 = "delete from contest_case_item_judge";
        this.entityManager.createNativeQuery(q4).executeUpdate();
        String q5 = "delete from contest_case_item_essay";
        this.entityManager.createNativeQuery(q5).executeUpdate();
        String q6 = "delete from contest_case_item_file";
        this.entityManager.createNativeQuery(q6).executeUpdate();
        String q7 = "delete from contest_case_item_case";
        this.entityManager.createNativeQuery(q7).executeUpdate();
        
        
        String q = "delete from ContestCase cq";
         this.entityManager.createQuery(q).executeUpdate();
    }
    
    @Override
    public void deleteContestCase(String id) {
        ContestCase c = this.entityManager.find(ContestCase.class, id);
        List<ContestCaseItemFile> files = c.getFileItems();
        for (ContestCaseItemFile f : files) {
            this.fqDAO.delFile(f.getId());
        }
        this.entityManager.remove(c);
    }
    
}
