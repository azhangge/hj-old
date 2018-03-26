package com.hjedu.course.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.hjedu.common.dao.UserCommentDAO;
import com.hjedu.course.entity.UserComment;
import com.huajie.util.CookieUtils;
import com.huajie.util.JsfHelper;
import com.huajie.util.SpringHelper;

@ManagedBean
@ViewScoped
public class ListUserComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	UserCommentDAO userCommentDAO = SpringHelper.getSpringBean("UserCommentDAO");
	
	private String businessId;
	
	private List<UserComment> userComments = new ArrayList<UserComment>();
	
	
	public List<UserComment> getUserComments() {
		return this.userComments;
	}


	public void setUserComments(List<UserComment> userComments) {
		this.userComments = userComments;
	}


	@PostConstruct
    public void init() {
		this.businessId = CookieUtils.getBusinessId(JsfHelper.getRequest());
		userComments = userCommentDAO.findAllValidUserComment(this.businessId);
    }
	
	public void deleteUserComment(String id) {
		Iterator<UserComment> iterator = userComments.iterator();
		while(iterator.hasNext()){
			UserComment userComment = iterator.next();
			if(userComment.getId().equals(id)){
				userCommentDAO.deleteUserComment(userComment);
				iterator.remove();
			}
		}
		
	}
}
