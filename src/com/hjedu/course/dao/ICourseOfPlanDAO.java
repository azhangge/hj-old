package com.hjedu.course.dao;

import java.util.List;

import com.hjedu.course.entity.CourseOfPlan;

public interface ICourseOfPlanDAO {

	CourseOfPlan findCourseOfPlan(String id);

	void deleteByPlanId(String id);

	List<CourseOfPlan> findCourseOfPlanByPlanId(String planId);

	void addCourseOfPlan(CourseOfPlan lt);
	
}
