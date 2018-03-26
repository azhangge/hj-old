package com.hjedu.customer.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.google.gson.annotations.Expose;
/**
 * 
 * 项目经历
 * 用户模块
 *
 */
@Entity
@Table(name = "project_experience")
public class ProjectExperience {
	@Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
	@Column(name = "create_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date createTime=new Date();
	@Column(name = "update_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date updateTime=new Date();;
	/**
	 * 名称
	 */
	@Column(name = "name")
    @Expose
	private String name;
	/**
	 * 开始时间
	 */
	@Column(name = "begin_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date beginTime;
	/**
	 * 结束时间
	 */
	@Column(name = "end_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date endTime;
    
	@ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.MERGE})
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	
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
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		if(endTime!=null&&endTime.getTime()==0){
			return null;
		}
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
