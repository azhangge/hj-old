package com.hjedu.examination.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hjedu.customer.entity.BbsUser;

/**
 * 集中考试用户表
 * @author h j
 *
 */
@Entity
@Table(name = "i_exam_user")
public class IntensiveExamAndUser {
	@Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();
	
	/**
	 * 关联考试
	 */
	@ManyToOne
	private Examination exam;
	
	/**
	 * 关联考生
	 */
	@ManyToOne
	private BbsUser user;
	
	/**
	 * 是否允许参加考试
	 */
	@Column(name = "if_allow")
	private boolean ifAllow;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Examination getExam() {
		return exam;
	}

	public void setExam(Examination exam) {
		this.exam = exam;
	}

	public BbsUser getUser() {
		return user;
	}

	public void setUser(BbsUser user) {
		this.user = user;
	}

	public boolean isIfAllow() {
		return ifAllow;
	}

	public void setIfAllow(boolean ifAllow) {
		this.ifAllow = ifAllow;
	}
	
	
}
