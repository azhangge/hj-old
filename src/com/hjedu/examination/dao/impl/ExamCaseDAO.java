package com.hjedu.examination.dao.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.examination.dao.IExamCaseDAO;
import com.hjedu.examination.dao.IExamChoiceStatisticDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.ExamCase;
import com.hjedu.examination.entity.ExamCaseFacet;
import com.hjedu.examination.entity.ExamCaseItemFile;
import com.hjedu.examination.service.IExamCaseCacheService;
import com.huajie.app.util.StringUtil;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

public class ExamCaseDAO implements IExamCaseDAO, Serializable {

    @PersistenceContext
    private EntityManager entityManager;
    IFileQuestionDAO fqDAO;
    private IExamChoiceStatisticDAO statisticDAO;
    //本字符串用于构建只查询成绩部分字段（如列表页）的HQL前半截（where前）
    private String simpleHQLPart = "select new "+ExamCaseFacet.class.getName()+"(cq.id, cq.name,cq.user,cq.examination,cq.genTime,cq.submitTime,cq.score,cq.ifPub,cq.ip,cq.timePassed,cq.stat,cq.ranking,cq.totalFullScore,cq.bbsScore,cq.ifSimulate,cq.ifPassed) from ExamCase cq ";

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public IExamChoiceStatisticDAO getStatisticDAO() {
        return statisticDAO;
    }

    public void setStatisticDAO(IExamChoiceStatisticDAO statisticDAO) {
        this.statisticDAO = statisticDAO;
    }

    public IFileQuestionDAO getFqDAO() {
        return fqDAO;
    }

    public void setFqDAO(IFileQuestionDAO fqDAO) {
        this.fqDAO = fqDAO;
    }

    @Override
    public void addExamCase(ExamCase m) {
        this.entityManager.persist(m);
    }

    /**
     * 删除关联的成绩统计信息，私有方法供内部使用，避免触发事务，降低速度
     *
     * @param caseId 成绩实例ID
     */
    public void removeStatisticByCase(String caseId) {
        String q = "delete from ExamChoiceStatistic ms where ms.examCaseId=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", caseId);
        qu.executeUpdate();
    }

    @Override
    public List<ExamCase> findAllExamCase() {
        String q = "Select cq from ExamCase cq where cq.examination.id!='7' order by cq.genTime desc";
        List<ExamCase> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ExamCase> findAllSubmittedExamCase() {
        String q = "Select cq from ExamCase cq where cq.examination.id!='7' and cq.stat='submitted' order by cq.genTime desc";
        List<ExamCase> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public List<ExamCase> findAllSavedExamCase() {
        String q = "Select cq from ExamCase cq where cq.examination.id!='7' and cq.stat='saved' order by cq.genTime desc";
        List<ExamCase> cqs = this.entityManager.createQuery(q).getResultList();
        return cqs;
    }

    @Override
    public boolean checkIfExists(String id) {
        String q = "Select ms.id from ExamCase ms where ms.id=:id";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", id);
        boolean empty = qu.getResultList().isEmpty();
        return !empty;
    }

    @Override
    public ExamCase findExamCase(String id) {
        ExamCase c = this.entityManager.find(ExamCase.class, id);
        return c;
    }

    @Override
    public List<ExamCase> findExamCaseByExamination(String id) {
        String q = "Select cq from ExamCase cq where cq.examination.id=:qid order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ExamCase> findExamCaseByExamination2(String id) {
        String q = "Select cq from ExamCase cq where cq.examination.id=:qid and cq.stat='submitted' order by cq.score asc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ExamCase> cqs = qu.getResultList();
        Map<String,String> map = new HashMap<String,String>();
        for(ExamCase ec : cqs){
        	map.put(ec.getUserId(), ec.getId());
        }
        String mapStr = "";
        Set set = map.keySet();
        for(Iterator iter = set.iterator(); iter.hasNext();) {
        	String key = (String)iter.next();//userId
        	String value = (String)map.get(key);//id
        	mapStr = mapStr + value + ";";
        }
        List<String> ids = StringUtil.idsToIdList(mapStr);
        String q1 = "Select cq from ExamCase cq where cq.examination.id=:qid and cq.id in :list";
        Query qu1 = this.entityManager.createQuery(q1);
        qu1.setParameter("qid", id);
        qu1.setParameter("list", ids);
        List<ExamCase> cqs1 = qu1.getResultList();
        return cqs1;
    }
    
    @Override
    public List<ExamCase> findFormalExamCaseByExamination(String id) {
        String q = "Select cq from ExamCase cq where cq.examination.id=:qid and cq.ifSimulate='0' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCase> findAllUnpubExamCase() {
        String q = "Select cq from ExamCase cq where cq.ifPub=false order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        List<ExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCase> findAllUnpubExamCaseByExam(String id) {
        String q = "Select cq from ExamCase cq where cq.examination.id=:qid and cq.ifPub=false order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCase> findSubmittedExamCaseByExamination(String id) {
        String q = "Select cq from ExamCase cq where cq.examination.id=:qid and cq.stat='submitted' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", id);
        List<ExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public ExamCase findTopScoreExamCase(String examId) {
        String q = "Select cq from ExamCase cq where cq.examination.id=:qid and cq.stat='submitted' order by cq.score desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", examId);
        qu.setMaxResults(1);
        List<ExamCase> cqs = qu.getResultList();
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
    public ExamCase findUserTopScoreExamCase(String examId,String userId) {
        String q = "Select cq from ExamCase cq where cq.examination.id=:qid and cq.stat='submitted' and cq.user.id=:id order by cq.score desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", examId);
        qu.setParameter("id", userId);
        qu.setMaxResults(1);
        List<ExamCase> cqs = qu.getResultList();
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
    public List<ExamCase> findExamCaseByExaminationAndUser(String examinationId, String uid) {
        String q = "Select cq from ExamCase cq where cq.examination.id=:qid and cq.user.id=:uid order by cq.genTime desc";
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("qid", examinationId);
        qu.setParameter("uid", uid);
        List<ExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCaseFacet> findSingleExamCaseForEachUserByExamination(String examId) {
        String q = this.simpleHQLPart + "where cq.examination.id=:eid order by cq.genTime desc";
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("eid", examId);
        List<ExamCaseFacet> cqs = qu.getResultList();
        Map map = new HashMap();
        for (ExamCaseFacet ec : cqs) {
            String key = ec.getUser().getId();
            ExamCaseFacet e = (ExamCaseFacet) map.get(key);
            if (e == null) {
                map.put(key, ec);
            } else if (ec.getScore() > e.getScore()) {
                map.put(key, ec);
            }
        }
        List<ExamCaseFacet> list = new LinkedList();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            list.add((ExamCaseFacet) map.get(key));
        }
        return list;
    }

    @Override
    public List<ExamCase> findSingleExamCaseForEachUser() {
        String q = "Select cq from ExamCase cq order by cq.genTime desc";
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        List<ExamCase> cqs = qu.getResultList();
        Map map = new HashMap();
        for (ExamCase ec : cqs) {
            String key = ec.getUser().getId() + ec.getExamination().getId();
            ExamCase e = (ExamCase) map.get(key);
            if (e == null) {
                map.put(key, ec);
            } else if (ec.getScore() > e.getScore()) {
                map.put(key, ec);
            }
        }
        List<ExamCase> list = new LinkedList();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            list.add((ExamCase) map.get(key));
        }
        return list;
    }

    @Override
    public List<ExamCase> findSingleExamCaseForEachUserByAdmin(AdminInfo admin, int offSet, int num) {
        String q = "Select cq from ExamCase cq where :admin member of cq.examination.admins order by cq.genTime desc";
        if(!admin.getGrp().equals("company")){
    		q = "Select cq from ExamCase cq order by cq.genTime desc";
    	}
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ExamCase> cqs = qu.getResultList();
        Map map = new HashMap();
        for (ExamCase ec : cqs) {
            String key = ec.getUser().getId() + ec.getExamination().getId();
            ExamCase e = (ExamCase) map.get(key);
            if (e == null) {
                map.put(key, ec);
            } else if (ec.getScore() > e.getScore()) {
                map.put(key, ec);
            }
        }
        List<ExamCase> list = new LinkedList();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            list.add((ExamCase) map.get(key));
        }
        return list;
    }

    @Override
    public List<ExamCaseFacet> findSingleExamCaseForEachUserByExamAndAdmin(String examId, AdminInfo admin, int offSet, int num) {
        String q = this.simpleHQLPart + "where cq.examination.id=:eid and :admin member of cq.examination.admins order by cq.genTime desc";
        if(!admin.getGrp().equals("company")){
    		q = this.simpleHQLPart + "where cq.examination.id=:eid order by cq.genTime desc";
    	}
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("eid", examId);
        if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ExamCaseFacet> cqs = qu.getResultList();
        Map<String, ExamCaseFacet> map = new HashMap();
        for (ExamCaseFacet ec : cqs) {
        	if(ec!=null){
        		BbsUser user = ec.getUser();
        		if(user!=null){
        			String key = ec.getUser().getId();
        			ExamCaseFacet e = (ExamCaseFacet) map.get(key);
        			if (e == null) {
        				map.put(key, ec);
        			} else if (ec.getScore() > e.getScore()) {
        				map.put(key, ec);
        			}
        		}
        	}
        }
        List<ExamCaseFacet> list = new LinkedList();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            list.add((ExamCaseFacet) map.get(key));
        }
        return list;
    }

    @Override
    public List<ExamCaseFacet> findExamCaseByExamAndAdmin(String examId, AdminInfo admin, int offSet, int num) {
        String q = this.simpleHQLPart + "where cq.examination.id=:eid and :admin member of cq.examination.admins order by cq.genTime desc";
        if(!admin.getGrp().equals("company")){
    		q = this.simpleHQLPart + "where cq.examination.id=:eid order by cq.genTime desc";
    	}
        //System.out.println(q);
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("eid", examId);
        if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ExamCaseFacet> cqs = qu.getResultList();

        return cqs;
    }

    @Override
    public List<ExamCase> findExamCaseByUser(String userId) {
        String q = "Select cq from ExamCase cq where cq.user.id=:id order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        List<ExamCase> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public List<ExamCaseFacet> findExamCaseFacetByUser(String userId) {
        String q = this.simpleHQLPart + "where cq.user.id=:id and cq.stat='submitted' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        List<ExamCaseFacet> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCaseFacet> findIntensiveExamCaseFacetByUser(String userId) {
    	String q = this.simpleHQLPart + "where cq.user.id=:id and cq.examination.examType=1 order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        List<ExamCaseFacet> cqs = qu.getResultList();
        return cqs;
    }
    
    @Override
    public long getTotalExamCaseBbsScore(String userId, Date btime, Date etime) {
        long t = 0;
        String q = "Select cq from ExamCase cq where cq.user.id=:id and cq.genTime<= :etime and cq.genTime>= :btime order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("id", userId);
        qu.setParameter("btime", btime);
        qu.setParameter("etime", etime);
        List<ExamCase> cqs = qu.getResultList();
        for (ExamCase ec : cqs) {
            if (ec.isIfPub()) {
                t += ec.getBbsScore();
            }
        }

        return t;
    }

    @Override
    public long getExamCaseNum(String businessId) {
        String q = "Select count(ms) from ExamCase ms where ms.examination.id !='7' and ms.businessId=:businessId and ms.stat='submitted' ";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        long num = ((Long) (qu.getResultList().get(0)));
        return num;
    }

    @Override
    public List<ExamCaseFacet> findLotsOfExamCase(int offSet, int num,String businessId) {
        String q = simpleHQLPart + "where cq.businessId=:businessId and cq.stat='submitted' order by cq.genTime desc";
        Query qu = this.entityManager.createQuery(q, ExamCaseFacet.class);
        qu.setParameter("businessId", businessId);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ExamCaseFacet> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCaseFacet> findOrderedExamCase(int offSet, int num, String field, String type,String businessId) {
        String q = this.simpleHQLPart + "where cq.businessId=:businessId and cq.stat='submitted' order by cq." + field + " " + type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ExamCaseFacet> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCaseFacet> findExamCaseByLike(Map<String, Object> fms, int offSet, int num,String businessId) {
        String q1 = this.simpleHQLPart + "where 1=1 and cq.businessId=:businessId and cq.stat='submitted' ";
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q1 + q2);
        qu.setParameter("businessId", businessId);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ExamCaseFacet> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public long getExamCaseNumByLike(Map<String, Object> fms,String businessId) {
        String q1 = "Select count(cq) from ExamCase cq where 1=1 and cq.businessId=:businessId and cq.stat='submitted' ";
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q1 + q2);
        qu.setParameter("businessId", businessId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public long getExamCaseNumByAdmin(AdminInfo admin,String businessId) {
        String q = "Select count(ms) from ExamCase ms where ms.businessId=:businessId and ms.stat='submitted' and :admin member of ms.examination.admins";
        if(!admin.getGrp().equals("company")){
    		q = "Select count(ms) from ExamCase ms where ms.businessId=:businessId and ms.stat='submitted' ";
    	}
        Query qu = this.entityManager.createQuery(q);
        if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        qu.setParameter("businessId", businessId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public List<ExamCaseFacet> findLotsOfExamCaseByAdmin(AdminInfo admin, int offSet, int num,String businessId) {
        String q = simpleHQLPart + "where cq.businessId=:businessId and cq.stat='submitted' and :admin member of cq.examination.admins order by cq.genTime desc";
        if(!admin.getGrp().equals("company")){
    		q = this.simpleHQLPart+"where cq.businessId=:businessId and cq.stat='submitted' order by cq.genTime desc";
    	}
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ExamCaseFacet> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCaseFacet> findOrderedExamCaseByAdmin(AdminInfo admin, int offSet, int num, String field, String type,String businessId) {
        String q = this.simpleHQLPart + "where cq.businessId=:businessId and cq.stat='submitted' and :admin member of cq.examination.admins order by cq." + field + " " + type;
        if(!admin.getGrp().equals("company")){
    		q = this.simpleHQLPart+"where cq.businessId=:businessId and cq.stat='submitted' order by cq." + field + " " + type;
    	}
        Query qu = this.entityManager.createQuery(q);
        if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        qu.setParameter("businessId", businessId);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ExamCaseFacet> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCase> findObsolatedExternalExamCase() {
        String q = "Select cq from ExamCase cq where cq.examination.ifExternal=true and cq.genTime<:dateTime";
        Query qu = this.entityManager.createQuery(q);
        long timeMill = System.currentTimeMillis() - ((long) 1000) * 60 * 60 * 24 * 15;
        qu.setParameter("dateTime", new Date(timeMill));
        List<ExamCase> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public List<ExamCaseFacet> findExamCaseByLikeAndAdmin(AdminInfo admin, Map<String, Object> fms, int offSet, int num,String businessId) {
    	String q1 = this.simpleHQLPart + "where cq.businessId=:businessId and cq.stat='submitted' and :admin member of cq.examination.admins ";
    	if(!admin.getGrp().equals("company")){
    		q1 = this.simpleHQLPart+"where 1=1 and cq.businessId=:businessId and cq.stat='submitted' ";
    	}
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q1 + q2);
        if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        qu.setParameter("businessId", businessId);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        List<ExamCaseFacet> cqs = qu.getResultList();
        return cqs;
    }

    @Override
    public long getExamCaseNumByLikeAndAdmin(AdminInfo admin, Map<String, Object> fms,String businessId) {
        String q1 = "Select count(cq) from ExamCase cq where cq.businessId=:businessId and cq.stat='submitted' and :admin member of cq.examination.admins ";
        if(!admin.getGrp().equals("company")){
    		q1 = "Select count(cq) from ExamCase cq where 1=1 and cq.businessId=:businessId and cq.stat='submitted' ";
    	}
        String q2 = " order by cq.genTime desc";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q1 = q1 + " and cq." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q1 + q2);
        qu.setParameter("businessId", businessId);
        if(admin.getGrp().equals("company")){
    		qu.setParameter("admin", admin);
    	}
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public void updateExamCase(ExamCase m) {
        this.entityManager.merge(m);
    }

    @Override
    public void updateBulkExamCase(Collection<ExamCase> ms) {
        for (ExamCase m : ms) {
            this.entityManager.merge(m);
        }
    }

    @Override
    public long getParticipateNumByExam(String examId) {
        String q = "Select count(ms) from ExamCase ms where ms.examination.id=:examId";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public long getParticipateNumByExamAndUser(String examId, String uid) {
        String q = "Select count(ms) from ExamCase ms where ms.examination.id=:examId and ms.ifSimulate ='0' and ms.user.id=:uid";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        qu.setParameter("uid", uid);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public long getParticipateNumByExamAndDate(String examId, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dd = sdf.format(date);//今天的日期字符串
        String dd1 = dd + " 00:00:00";
        String dd2 = dd + " 23:59:59";
        Date date1 = new Date();
        Date date2 = new Date();
        try {
            date1 = sdf2.parse(dd1);
            date2 = sdf2.parse(dd2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String q = "Select count(ms) from ExamCase ms where ms.examination.id=:examId and ms.genTime<:date2 and ms.genTime>=:date1";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("examId", examId);
        qu.setParameter("date1", date1);
        qu.setParameter("date2", date2);
        long num = ((Long) (qu.getResultList().get(0))).longValue();
        return num;
    }

    @Override
    public void deleteExamCase(String id) {
        ExamCase c = this.entityManager.find(ExamCase.class, id);
        this.removeStatisticByCase(id);
        if (c != null) {
            List<ExamCaseItemFile> files = c.getFileItems();
            if (files != null) {
                for (ExamCaseItemFile f : files) {
                    this.fqDAO.delFile(f.getId());
                }
            }
            this.entityManager.remove(c);
            //更新成绩缓存
            IExamCaseCacheService cache = SpringHelper.getSpringBean("ExamCaseCacheService");
            cache.deleteExamCase(c.getId());
        }
    }

    @Override
    public void deleteBulkExamCase(List<ExamCase> cases) {
        for (final ExamCase c : cases) {
            this.deleteExamCase(c.getId());
        }
    }

    @Override
    public void deleteBulkExamCaseFacet(List<ExamCaseFacet> cases) {
        for (final ExamCaseFacet c : cases) {
            this.removeStatisticByCase(c.getId());
            this.deleteExamCase(c.getId());
        }
    }

    @Override
    public void deleteBulkExamCaseByExam(String id) {
        List<ExamCase> cases = this.findExamCaseByExamination(id);
        this.deleteBulkExamCase(cases);
    }

    /**
     * 发布ID在IDS中的全部成绩
     *
     * @param ids
     */
    @Override
    public void publishExamCases(List<String> ids) {
        if (ids == null) {
            return;
        } else if (ids.isEmpty()) {
            return;
        }
        String q = "update ExamCase ec set ec.ifPub=true where ec.id in :list";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("list", ids);
        qu.executeUpdate();
    }
    
    @Override
    public void setExamCasesPass(List<String> ids) {
        if (ids == null) {
            return;
        } else if (ids.isEmpty()) {
            return;
        }
        String q = "update ExamCase ec set ec.ifPassed=true where ec.id in :list";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("list", ids);
        qu.executeUpdate();
    }

    @Override
    public void deleteAllExamCase() {
        //删除所有成绩条目
        String q1 = "delete from exam_case_item_choice";
        this.entityManager.createNativeQuery(q1).executeUpdate();
        String q2 = "delete from exam_case_item_multi_choice";
        this.entityManager.createNativeQuery(q2).executeUpdate();
        String q3 = "delete from exam_case_item_fill";
        this.entityManager.createNativeQuery(q3).executeUpdate();
        String q4 = "delete from exam_case_item_judge";
        this.entityManager.createNativeQuery(q4).executeUpdate();
        String q5 = "delete from exam_case_item_essay";
        this.entityManager.createNativeQuery(q5).executeUpdate();
        String q6 = "delete from exam_case_item_file";
        this.entityManager.createNativeQuery(q6).executeUpdate();
        String q7 = "delete from exam_case_item_case";
        this.entityManager.createNativeQuery(q7).executeUpdate();
        //删除所有作答统计
        String q8 = "delete from e_choice_statistic";
        this.entityManager.createNativeQuery(q8).executeUpdate();
        //删除所有抽卷记录
        String q9 = "delete from exam_case_log";
        this.entityManager.createNativeQuery(q9).executeUpdate();

        String q = "delete from exam_case";
        this.entityManager.createNativeQuery(q).executeUpdate();
        //更新成绩缓存
        IExamCaseCacheService cache = SpringHelper.getSpringBean("ExamCaseCacheService");
        cache.reBuildCache(true);

    }

    public static void main(String args[]) {

        IExamCaseDAO dao = SpringHelper.getSpringBean("ExamCaseDAO");
        List<ExamCaseFacet> cs = dao.findLotsOfExamCase(0, 10,CookieUtils.getBusinessId(JsfHelper.getRequest()));
        System.out.println(cs.size());
        List<String> ids = new ArrayList();
        for (ExamCaseFacet ec : cs) {
            //ids.add(ec.getId());
            System.out.println(ec.getExamination().getName());
        }
        dao.publishExamCases(ids);
    }

	@Override
	public List<ExamCase> findFinishedExamCasesByUser(String uesrId) {
		String q = "Select ms from ExamCase ms where ms.user.id=:uid and ms.ifPassed=1";
        Query qu = this.entityManager.createQuery(q).setParameter("uid", uesrId);
        return qu.getResultList();
	}

}
