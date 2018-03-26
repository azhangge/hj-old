package com.hjedu.course.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.Teacher;
/**
 * 
 * 审批流程表
 *
 */
@Entity
@Table(name = "course_approve_history")
public class CourseApproveHistory {
	@Id
    @Column(name = "id")
	private String id = UUID.randomUUID().toString();

	/**
	 * 审批的课程
	 */
	@OneToOne
	@JoinColumn(name = "lessonType_id")
	private LessonType lessonType;

	/**
	 * 审批人
	 */
	@OneToOne
	@JoinColumn(name = "approveTeacher_id")
	private Teacher approveTeacher;
	
	/**
	 * 创建人
	 */
	@OneToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	
	@Column(name = "create_time")
    @Expose
	private Date createTime = new Date();
	
	@Column(name = "reason")
    @Expose
	private String reason;

    /**
     * 0免审批状态 1初始状态 2待审批 3审批中 4通过 5驳回
     */
	@Column(name = "approved")
    @Expose
	private int approved=-1;
	
    /**
     * 0存在 1已删除
     */
	@Column(name = "deleted")
    @Expose
	private boolean deleted=false;
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getApproved() {
		return approved;
	}

	public void setApprove(int approved) {
		this.approved = approved;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}
	
	public Teacher getApproveTeacher() {
		return approveTeacher;
	}

	public void setApproveTeacher(Teacher approveTeacher) {
		this.approveTeacher = approveTeacher;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
