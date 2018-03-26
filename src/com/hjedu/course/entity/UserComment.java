/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hjedu.course.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * 课程模块
 * 用户查看课程记录
 * @author h j
 *
 */
@Entity
@Table(name = "user_comment")
//@PrimaryKeyJoinColumn(name="course_log_id")
public class UserComment extends UserAndCourse implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 评级
	 */
	@Column(name = "star")
	@Expose
	private int star;
	/**
	 * 评价
	 */
	@Column(name = "costs")
	@Expose
	private String comment;
	
	@Column(name = "business_id")
	@Expose
	private String businessId;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
