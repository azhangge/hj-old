package com.hjedu.course.dao;

import java.util.Map;

import com.hjedu.common.dao.BaseDAO;
import com.hjedu.examination.entity.CourseType;

public interface CourseTypeDAO  extends BaseDAO<CourseType> {
	
	Map<String, Object> getAppCourseType(int firstSize, int pageSize,String businessId);
	
	Map<String, Object> pageAppCourseTypeByFloorShow(int firstSize, int pageSize,String businessId);
}
