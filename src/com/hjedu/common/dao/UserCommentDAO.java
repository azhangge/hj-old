package com.hjedu.common.dao;

import java.util.List;

import com.hjedu.course.entity.UserComment;

public interface UserCommentDAO extends BaseDAO<UserComment> {

	List<UserComment> findByUserAndCourse(String userId, String courseId);
	
	List<UserComment> findAllValidUserComment(String businessId);
	
	void deleteUserComment(UserComment userComment);
}
