package com.hjedu.course.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.Teacher;

/**
 * 课程模块
 * 基础课程类
 */
@Entity
@Table(name = "base_course")
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseCourse implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 删除标记（0：正常；1：删除；2：审核；）
	 */
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";
	
	/**
	 * 实体编号（唯一标识）
	 */
	@Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
	protected String id = this.getClass().getName().replace(this.getClass().getPackage().getName()+".", "")+"_"+UUID.randomUUID().toString();
	
	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    protected Date createTime = new Date();
	
	/**
	 * 最后修改时间
	 */
	@Column(name = "last_modify_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    protected Date lastModifyTime = new Date();
	
    @Column(name = "delete_flag")
    @Expose
    protected int deleteFlag;
    
    /**
     * 临时属性，是否被选中
     */
    @Transient
    protected boolean selected = false;
	
	@Column(name = "name")
	@Expose
	private String name;

	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	
	/**
	 * 课程简介
	 */
	@Column(name = "introduction")
	@Expose
	private String introduction;

	/**
	 * 课程开始时间（默认当前时间）
	 */
	@Column(name = "begin_time")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	protected Date beginTime = new Date();

	/**
	 * 课程结束时间（默认开始时间一年后）
	 */
	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	protected Date endTime = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 365L);

	/**
	 * 课程类型：0：课程；1：学习计划
	 */
	@Column(name = "course_type")
	@Expose
	private int courseType;

	@Column(name = "picture")
	@Expose
	private String picture = "servlet/ShowImage?id=100";

	/**
	 * 总课时数
	 */
	@Column(name = "total_period")
	@Expose
	private int totalPeriod;

	/**
	 * 标签（多个用；隔开，最多10个）
	 */
	@Column(name = "course_tags")
	@Expose
	private String courseTags;

	/**
	 * 是否开放(0不开放 1开放)
	 */
	@Basic(optional = false)
	@Column(name = "enabled")
	private boolean enabled;

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public BaseCourse() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getCourseType() {
		return courseType;
	}

	public void setCourseType(int courseType) {
		this.courseType = courseType;
	}

	public int getTotalPeriod() {
		return totalPeriod;
	}

	public void setTotalPeriod(int totalPeriod) {
		this.totalPeriod = totalPeriod;
	}

	public String getCourseTags() {
		return courseTags;
	}

	public void setCourseTags(String courseTags) {
		this.courseTags = courseTags;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "[ name=" + name + " ]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
