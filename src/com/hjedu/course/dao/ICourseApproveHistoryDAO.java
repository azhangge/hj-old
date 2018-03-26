package com.hjedu.course.dao;

import java.util.List;

import com.hjedu.course.entity.CourseApproveHistory;

public interface ICourseApproveHistoryDAO {

	void addCourseApproveHistory(CourseApproveHistory m);

	List<CourseApproveHistory> findAllCourseApproveHistory();

	void deleteByCourseApproveHistoryById(String id);

	void falseDeleteByCourseApproveHistoryById(String id);

	List<CourseApproveHistory> findCourseApproveHistoryByApprover(String teacherId, int teacherType,
			List<Integer> approvalStatus, int firstSize, int pageSize);

	long findCourseApproveHistoryCountByApprover(String teacherId, int teacherType, List<Integer> approvalStatus);

	void falseDeleteByCourseApproveHistoryByIds(List<String> ids);
	
}
