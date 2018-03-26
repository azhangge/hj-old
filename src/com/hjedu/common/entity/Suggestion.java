package com.hjedu.common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;

/**
 * 用户建议
 */
@Entity
@Table(name = "suggestion")
public class Suggestion implements Serializable{
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
	
    /**
     * 1：课程；2：考试；3：练习；4：其他
     */
    @Column(name = "suggestion_type")
	@Expose
    private int suggestionType;
    
    /**
     * 建议
     */
	@Column(name = "suggestion")
	@Expose
	private String suggestion;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private BbsUser user;
	
	@Column(name = "picture")
	@Expose
	private String picture;
	
	@Column(name = "reply")
	@Expose
	private String reply;

	@Column(name = "business_id")
	private String businessId;

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
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

	public int getSuggestionType() {
		return suggestionType;
	}

	public void setSuggestionType(int suggestionType) {
		this.suggestionType = suggestionType;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public BbsUser getUser() {
		return user;
	}

	public void setUser(BbsUser user) {
		this.user = user;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
	
}
