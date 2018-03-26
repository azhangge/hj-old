package com.hjedu.course.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.hjedu.course.dao.ICourseOfPlanDAO;
import com.hjedu.course.entity.CourseOfPlan;
import com.hjedu.course.entity.LessonType;

public class CourseOfPlanDAO implements ICourseOfPlanDAO, Serializable {
	private static final long serialVersionUID = 1L;

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public CourseOfPlan findCourseOfPlan(String id){
		return entityManager.find(CourseOfPlan.class, id);
	}

	@Override
	public void deleteByPlanId(String id) {
//		LessonType yp = (LessonType) this.entityManager.find(LessonType.class, paramString);
		List<CourseOfPlan> cops = findCourseOfPlanByPlanId(id);
		for(CourseOfPlan cop : cops){
			this.entityManager.remove(cop);
		}
	}
	
	@Override
	public List<CourseOfPlan> findCourseOfPlanByPlanId(String planId){
		String q = "select cop from CourseOfPlan cop where cop.studyPlan.id=:id";
        List<CourseOfPlan> ps = entityManager.createQuery(q).setParameter("id", planId).getResultList();
		return ps;
	}

	@Override
	public void addCourseOfPlan(CourseOfPlan lt) {
		entityManager.persist(lt);
	}
}
