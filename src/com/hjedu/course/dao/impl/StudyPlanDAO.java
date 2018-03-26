package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.hjedu.course.dao.ILessonTypeDAO;
import com.hjedu.course.dao.IStudyPlanDAO;
import com.hjedu.course.entity.LessonType;
import com.hjedu.course.entity.StudyPlan;
import com.hjedu.examination.dao.IExaminationDAO;
import com.hjedu.examination.entity.Examination;
import com.hjedu.platform.entity.OperationLog;
import com.huajie.app.util.QueryUtil;
import com.huajie.app.util.StringUtil;
import com.huajie.util.SpringHelper;

@Repository("StudyPlanDAO")
public class StudyPlanDAO implements IStudyPlanDAO, Serializable {

	private static final long serialVersionUID = 1L;
	@PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<StudyPlan> findAllStudyPlan() {
        String q = "Select us from StudyPlan us order by us.ord";
        List<StudyPlan> ais = entityManager.createQuery(q).getResultList();
        return ais;
    }
    
    @Override
    public StudyPlan findStudyPlan(String id) {
        if (id != null) {
            StudyPlan StudyPlan = this.entityManager.find(StudyPlan.class, id);
            return StudyPlan;
        } else {
            return null;
        }

    }
    
    @SuppressWarnings("unchecked")
	@Override
    public StudyPlan findStudyPlanByUrn(String urn) {
        String q = "Select us from StudyPlan us where us.name=:urn";
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("urn", urn);
        List<StudyPlan> as = qu.getResultList();
        if (as.isEmpty()) {
            return null;
        } else {
            StudyPlan ai = (StudyPlan) as.get(0);
            return ai;
        }
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<StudyPlan> findStudyPlansLikeUrn(final String urn) {
        String q = "Select us from StudyPlan us where us.name like '%" + urn + "%'";
        List<StudyPlan> as = entityManager.createQuery(q).getResultList();
        return as;
    }

    @Override
    public void updateStudyPlan(StudyPlan u) {
        this.entityManager.merge(u);
    }

    @Override
    public void deleteStudyPlanByUrn(String urn) {
        if (urn != null) {
            StudyPlan u = this.findStudyPlanByUrn(urn);
            if (u != null) {
                this.entityManager.remove(u);
            }
        }
    }
	@Override
	public void addStudyPlan(StudyPlan StudyPlan) {
		this.entityManager.persist(StudyPlan);
	}
	@Override
	public void deleteStudyPlan(String id) {
		if (id != null) {
            StudyPlan u = this.findStudyPlan(id);
            if (u != null) {
            	this.entityManager.remove(u);
            }
        }
	}
	@Override
	public List<StudyPlan> findAllStudyPlanOrderByDept() {
		String q = "Select us from StudyPlan us order by us.name";
        List<StudyPlan> ais = entityManager.createQuery(q).getResultList();
        return ais;
	}
	@Override
	public List<StudyPlan> findStudyPlanByUserId(String uid) {
		String q = "select us from StudyPlan us where us.userStr like '%"+uid+"%'";
		Query qu=entityManager.createQuery(q);
		List<StudyPlan> ps = qu.getResultList();
		return ps;
	}
	@Override
	public List<LessonType> findCoursesByStudyPlan(StudyPlan StudyPlan){
		if(StudyPlan==null||StudyPlan.getCourseStr()==null){
			return null;
		}
		ILessonTypeDAO lessonTypeDAO = SpringHelper.getSpringBean("LessonTypeDAO");
		String[] ids = StudyPlan.getCourseStr().split(";");
		List<String> idList = new LinkedList<>(Arrays.asList(ids));
		return lessonTypeDAO.findLessonTypeByIdList(idList);
	}
	@Override
	public List<Examination> findExamsByStudyPlan(StudyPlan StudyPlan){
		if(StudyPlan==null||StudyPlan.getExamsStr()==null){
			return null;
		}
		IExaminationDAO examinationDAO = SpringHelper.getSpringBean("ExaminationDAO");
		String[] ids = StudyPlan.getExamsStr().split(";");
		List<String> idList = new LinkedList<>(Arrays.asList(ids));
		return examinationDAO.findExamsByIdList(idList);
	}
	
	@Override
	public List<StudyPlan> findStudyPlansByOrder(String str,int firstSize,int pageSize,boolean ifDesc, String businessId) {
		String q = "select yis from StudyPlan yis where 1=1 and yis.businessId=:businessId ";
		if(StringUtil.isNotEmpty(str)){
			q = q+" order by "+str;
			if(ifDesc){
				q = q + " desc ";
			}
		}
		Query qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
		if(pageSize>0){
			if(firstSize>=0){
				qu.setFirstResult(firstSize);
			}
			qu.setMaxResults(pageSize);
		}
		List<StudyPlan> ps = qu.getResultList();
		return ps;
	}
	
	@Override
	public List<StudyPlan> findStudyPlanByUserId(String uid,int firstSize,int pageSize) {
		String q = "select us from StudyPlan us where us.userStr like '%"+uid+"%'";
		Query qu=entityManager.createQuery(q);
		setQuerySize(qu, firstSize, pageSize);
		@SuppressWarnings("unchecked")
		List<StudyPlan> ps = qu.getResultList();
		return ps;
	}
	
	private void setQuerySize(Query qu,int firstSize,int pageSize){
		if(pageSize>0){
			if(firstSize>=0){
				qu.setFirstResult(firstSize);
			}
			qu.setMaxResults(pageSize);
		}
	}
	
	@Override
	public List<StudyPlan> findStudyPlansByOrder(String str,int firstSize,int pageSize,boolean ifDesc,List<String> idlist,boolean ifIn) {
		String q = "select yis from StudyPlan yis where 1=1 ";
		if(idlist!=null&&idlist.size()>0){
			if(ifIn){
				q = q + " and yis.id in :list";
			}else{
				q = q + " and yis.id not in :list";
			}
		}
		if(StringUtil.isNotEmpty(str)){
			q = q+" order by yis."+str;
			if(ifDesc){
				q = q+" desc ";
			}
		}
		Query qu=entityManager.createQuery(q);
		if(idlist!=null&&idlist.size()>0){
			qu.setParameter("list", idlist);
		}
		QueryUtil.setQuerySize(qu, firstSize, pageSize);
		@SuppressWarnings("unchecked")
		List<StudyPlan> ps = qu.getResultList();
		return ps;
	}
	@Override
	public long getStudyPlansNumByOrder(List<String> idlist, boolean ifIn,String businessId) {
		String q = "select count(yis) from StudyPlan yis where 1=1 and yis.businessId=:businessId ";
		if(idlist!=null&&idlist.size()>0){
			if(ifIn){
				q = q + " and yis.id in :list";
			}else{
				q = q + " and yis.id not in :list";
			}
		}
		Query qu=entityManager.createQuery(q);
		qu.setParameter("businessId", businessId);
		if(idlist!=null&&idlist.size()>0){
			qu.setParameter("list", idlist);
		}
		return Long.parseLong(String.valueOf(qu.getResultList().get(0)));
	}
	
	@Override
	public List<StudyPlan> findStudyPlansByFilter(Map<String, Object> fms,String businessId) {
        String q = "Select ol from StudyPlan ol where 1=1 and ol.businessId=:businessId ";
        for (Iterator<String> it = fms.keySet().iterator(); it.hasNext();) {
            String filterProperty = it.next();
            String filterValue = fms.get(filterProperty).toString();
            if (filterValue != null) {
                q = q + " and ol." + filterProperty + " like '%" + filterValue + "%' ";
            }
        }
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        @SuppressWarnings("unchecked")
		List<StudyPlan> cqs = qu.getResultList();
        return cqs;
    }
	
	@Override
	public List<StudyPlan> findOrderedStudyPlans(int offSet, int num, String field, String type,String businessId) {
        String q = "Select  ol from StudyPlan ol where 1=1 and ol.businessId=:businessId order by ol."+field+" "+type;
        Query qu = this.entityManager.createQuery(q);
        qu.setParameter("businessId", businessId);
        qu.setFirstResult(offSet);
        qu.setMaxResults(num);
        @SuppressWarnings("unchecked")
		List<StudyPlan> cqs = qu.getResultList();
        return cqs;
	}
}
