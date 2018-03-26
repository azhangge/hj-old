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
 * 职称
 * 用户模块
 *
 */
@Entity
@Table(name = "job_title")
public class JobTitle {
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
	 * 职称名称
	 */
	@Column(name = "name")
    @Expose
	private String name;
	/**
	 * 获取时间
	 */
	@Column(name = "gain_time")
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date gainTime;
	/**
	 * 职称等级
	 */
	@Column(name = "title_level")
    @Expose
	private String titleLevel;
	/**
	 * 职称图片
	 */
	@Column(name = "picture")
    @Expose
	private String picture;
    
	@ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.MERGE})
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Date getGainTime() {
		return gainTime;
	}
	public void setGainTime(Date gainTime) {
		this.gainTime = gainTime;
	}
	public String getTitleLevel() {
		return titleLevel;
	}
	public void setTitleLevel(String titleLevel) {
		this.titleLevel = titleLevel;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
}
