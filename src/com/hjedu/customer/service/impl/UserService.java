package com.hjedu.customer.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hazelcast.util.CollectionUtil;
import com.hjedu.customer.dao.IBbsUserDAO;
import com.hjedu.customer.entity.BbsUser;
import com.hjedu.customer.service.IUserService;
import com.huajie.app.util.FileUtil;
import com.huajie.util.SpringHelper;

@Service("UserService")
public class UserService implements IUserService{
	private IBbsUserDAO userDAO;
	
	public IBbsUserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(IBbsUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public void updateUserCollectionCourses(BbsUser user,List<String> courseList,boolean addOrRemove){
		String ccs = user.getCollectionCourses();
		if(ccs==null){
			ccs = "";
		}
		if(CollectionUtil.isNotEmpty(courseList)){
			if(addOrRemove){//添加收藏
				for(String courseId : courseList){
					if(!ccs.contains(courseId)){
						ccs = ccs+courseId+";";
					}
				}
			}else{//移除收藏
				for(String courseId : courseList){
					ccs = FileUtil.removePartOfStr(ccs, courseId);
				}
			}
			user.setCollectionCourses(ccs);
			userDAO.updateBbsUser(user);
		}
	}
}
