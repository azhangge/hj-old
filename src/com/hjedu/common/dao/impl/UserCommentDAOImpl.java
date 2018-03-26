package com.hjedu.common.dao.impl;

import java.util.List;

import javax.persistence.Query;

import com.hjedu.common.dao.UserCommentDAO;
import com.hjedu.course.entity.UserComment;
import com.huajie.app.util.StringUtil;

public class UserCommentDAOImpl extends BaseDAOImpl<UserComment> implements UserCommentDAO {
	@Override
	public List<UserComment> findByUserAndCourse(String userId,String courseId){
		String q = "select o from UserComment o where 1=1 ";
		if(StringUtil.isNotEmpty(userId)){
			q += " and o.user.id=:userId ";
		}
		if(StringUtil.isNotEmpty(courseId)){
			q += " and o.course.id=:courseId ";
		}
		Query query = this.getEntityManager().createQuery(q);
		if(StringUtil.isNotEmpty(userId)){
			query.setParameter("userId", userId);
		}
		if(StringUtil.isNotEmpty(courseId)){
			query.setParameter("courseId", courseId);
		}
		@SuppressWarnings("unchecked")
		List<UserComment> l = query.getResultList();
		return l;
	}
	
	@Override
	public List<UserComment> findAllValidUserComment(String businessId) {
		String q = "Select uc from UserComment uc where uc.businessId = :businessId and uc.deleteFlag = 0 order by uc.createTime desc";
        Query query = this.getEntityManager().createQuery(q);
        query.setParameter("businessId", businessId);
		List<UserComment> ucs = query.getResultList();
        return ucs;
	}

	@Override
	public void deleteUserComment(UserComment userComment) {
		userComment.setDeleteFlag(1);
		this.getEntityManager().merge(userComment);
	}
}
