package com.hjedu.course.dao.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import com.hjedu.common.dao.impl.BaseDAOImpl;
import com.hjedu.course.dao.CourseTypeDAO;
import com.hjedu.examination.entity.CourseType;

public class CourseTypeDAOImpl extends BaseDAOImpl<CourseType> implements CourseTypeDAO {

	@Override
	public Map<String, Object> getAppCourseType(int firstSize,int pageSize,String businessId) {
		String q = "select t.id,t.name,t.picture "
				+ "from course_type t where t.id!='"+businessId+"' and t.business_Id='"+businessId+"' and t.type_level = 1";
		Query qu = this.getEntityManager().createNativeQuery(q);
		qu.setFirstResult(firstSize);
		qu.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Object[]> rows = qu.getResultList();  
		List<Map<String, Object>> types = new LinkedList<>();
	    for (Object row : rows) {  
	        Object[] cells = (Object[]) row;  
	        Map<String, Object> map = new HashMap<>();
	        map.put("id", cells[0]);
	        map.put("name", cells[1]);
	        map.put("picture", cells[2]);
	        types.add(map);
	    } 
	    Map<String, Object> map = new HashMap<>();
	    map.put("list", types);
		return map;
	}

	@Override
	public Map<String, Object> pageAppCourseTypeByFloorShow(int firstSize, int pageSize, String businessId) {
		String q = "select t.id,t.name,t.picture "
				+ "from course_type t where t.business_Id='"+businessId+"' and t.floor_show = 1";
		Query qu = this.getEntityManager().createNativeQuery(q);
		qu.setFirstResult(firstSize);
		qu.setMaxResults(pageSize);
		@SuppressWarnings("unchecked")
		List<Object[]> rows = qu.getResultList();  
		List<Map<String, Object>> types = new LinkedList<>();
	    for (Object row : rows) {  
	        Object[] cells = (Object[]) row;  
	        Map<String, Object> map = new HashMap<>();
	        map.put("id", cells[0]);
	        map.put("name", cells[1]);
	        map.put("picture", cells[2]);
	        types.add(map);
	    } 
	    Map<String, Object> map = new HashMap<>();
	    map.put("list", types);
		return map;
	}
}