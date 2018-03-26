package com.hjedu.course.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;
import com.hjedu.platform.entity.BbsFileModel;

/**
 * 课程模块
 * 基础课程类
 */
@Entity
@Table(name = "chapter")
public class Chapter implements Serializable{
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
    @JoinColumn(name = "course_id")
	private HJCourse course;
	
	/**
	 * 章节序号
	 */
	@Column(name = "order_num")
	@Expose
	private int orderNum;
	
	@OneToOne(cascade={CascadeType.ALL},mappedBy="chapter")
	@JoinColumn(name = "file_id")
	private BbsFileModel file;
	
	/**
	 * 课时
	 */
	@Column(name = "period")
	@Expose
	private int period;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HJCourse getCourse() {
		return course;
	}

	public void setCourse(HJCourse course) {
		this.course = course;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public BbsFileModel getFile() {
		return file;
	}

	public void setFile(BbsFileModel file) {
		this.file = file;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}
	
}
