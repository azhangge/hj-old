package com.hjedu.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

@Entity
@Table(name = "base_entity")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity implements Serializable{

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
	protected String id = this.getClass().getName()+UUID.randomUUID().toString();
	
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
}
