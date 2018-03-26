package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.course.dao.ICourseApproveHistoryDAO;
import com.hjedu.course.entity.CourseApproveHistory;
import com.hjedu.course.entity.LessonType;
import com.hjedu.customer.entity.AdminInfo;
import com.hjedu.examination.dao.IContestCaseDAO;
import com.hjedu.examination.dao.IFileQuestionDAO;
import com.hjedu.examination.entity.CourseType;
import com.hjedu.examination.entity.contest.ContestCase;
import com.hjedu.examination.entity.contest.ContestCaseItemFile;
import com.huajie.app.util.QueryUtil;
import com.huajie.app.util.StringUtil;

public class CourseApproveHistoryDAO implements ICourseApproveHistoryDAO, Serializable {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Override
    public void deleteByCourseApproveHistoryById(String id) {
    	CourseApproveHistory cah = entityManager.find(CourseApproveHistory.class, id);
        entityManager.remove(cah);
    }
    
    @Override
    public void falseDeleteByCourseApproveHistoryById(String id) {
    	CourseApproveHistory cah = entityManager.find(CourseApproveHistory.class, id);
        cah.setDeleted(true);
        this.entityManager.merge(cah);
    }
    
    @Override
    public void falseDeleteByCourseApproveHistoryByIds(List<String> idlist) {
    	if(idlist!=null && idlist.size()>0){
    		String q = "Update CourseApproveHistory cah set cah.deleted=1 where cah.id in :list";
    		Query qu=entityManager.createQuery(q);
    		qu.setParameter("list", idlist);
    		qu.executeUpdate();
    	}
    }
    
    @Override
    public void addCourseApproveHistory(CourseApproveHistory m) {
        this.entityManager.persist(m);
    }
    
    @Override
    public List<CourseApproveHistory> findAllCourseApproveHistory() {
        String q = "Select cah from CourseApproveHistory cah";
        List<CourseApproveHistory> cahs = this.entityManager.createQuery(q).getResultList();
        return cahs;
    }
    
    @Override
    public List<CourseApproveHistory> findCourseApproveHistoryByApprover(String teacherId , int teacherType, List<Integer> approvalStatus, int firstSize, int pageSize){
    	String q = "select cah from CourseApproveHistory cah where 1=1 ";
    	if(StringUtil.isNotEmpty(teacherId)){
			if(teacherType==0){
				q = q+" and cah.teacher.id=:teacherId ";
			}else if(teacherType==1){
				q = q+" and cah.approveTeacher.id=:teacherId ";
			}
    	}
		if(approvalStatus!=null && approvalStatus.size()>0){
			q = q+" and cah.approved in :list";
		}
		q = q+" and cah.deleted=0 ";
		Query query = entityManager.createQuery(q);
		if(StringUtil.isNotEmpty(teacherId)){
			query.setParameter("teacherId", teacherId);
		}
		if(approvalStatus!=null && approvalStatus.size()>0){
			query.setParameter("list", approvalStatus);
		}
		QueryUtil.setQuerySize(query, firstSize, pageSize);
		List<CourseApproveHistory> results = query.getResultList();
		return results;
    }

    @Override
    public long findCourseApproveHistoryCountByApprover(String teacherId , int teacherType, List<Integer> approvalStatus){
    	String q = "select count(cah) from CourseApproveHistory cah where 1=1 ";
    	if(StringUtil.isNotEmpty(teacherId)){
			if(teacherType==0){
				q = q+" and cah.teacher.id=:teacherId ";
			}else if(teacherType==1){
				q = q+" and cah.approveTeacher.id=:teacherId ";
			}
    	}
		if(approvalStatus!=null && approvalStatus.size()>0){
			q = q+" and cah.approved in :list";
		}
		q = q+" and cah.deleted=0 ";
		Query query = entityManager.createQuery(q);
		if(StringUtil.isNotEmpty(teacherId)){
			query.setParameter("teacherId", teacherId);
		}
		if(approvalStatus!=null && approvalStatus.size()>0){
			query.setParameter("list", approvalStatus);
		}
		return Long.parseLong(String.valueOf(query.getResultList().get(0)));
    }
    
}
