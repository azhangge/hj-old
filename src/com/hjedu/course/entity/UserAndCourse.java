/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hjedu.course.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hjedu.common.entity.BaseEntity;
import com.hjedu.customer.entity.BbsUser;

/**
 * 课程模块
 * 用户和课程关联表
 * @author h j
 */
@Entity
//@Table(name = "user_and_course")
//@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserAndCourse extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToOne
    @JoinColumn(name = "course_id")
	private LessonType course;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
	private BbsUser user;

	public LessonType getCourse() {
		return course;
	}

	public void setCourse(LessonType course) {
		this.course = course;
	}

	public BbsUser getUser() {
		return user;
	}

	public void setUser(BbsUser user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
