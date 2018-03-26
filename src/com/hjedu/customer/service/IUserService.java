package com.hjedu.customer.service;

import java.util.List;

import com.hjedu.customer.entity.BbsUser;

public interface IUserService {
	/**
	 * 修改用户课程收藏
	 * @param user
	 * @param courseList 课程id集合
	 * @param addOrRemove true为添加收藏，false为取消收藏
	 */
	void updateUserCollectionCourses(BbsUser user, List<String> courseList, boolean addOrRemove);

}
