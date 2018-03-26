package com.hjedu.examination.dao;

import java.util.List;

import com.hjedu.examination.entity.IntensiveExamAndUser;

public interface IIntensiveExamAndUserDAO {
	/**
	 * 根据考试id获取关联对象
	 * @param examId
	 * @return
	 */
	List<IntensiveExamAndUser> getIntensiveExamAndUserByExamId(String examId);
	
	/**
	 * 根据用户id获取关联对象
	 * @param userId
	 * @return
	 */
	List<IntensiveExamAndUser> getIntensiveExamAndUserByUserId(String userId);
	
	void updateIntensiveExamAndUser(IntensiveExamAndUser u);
	
	IntensiveExamAndUser getIntensiveExamAndUserByUserExamId(String userId,String examId);

	void addIntensiveExamAndUser(IntensiveExamAndUser u);
}
