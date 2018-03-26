package com.hjedu.platform.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.annotations.Expose;
/**
 * 
 * 通知用户表
 * 系统模块
 * 
 */
@Entity
@Table(name = "notice_user")
public class NoticeUser {
    @Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
	
    @Column(name = "notice_id")
    @Expose
    private String noticeId;
    
    @Column(name = "user_id")
    @Expose
    private String userId;  
    
	@Basic(optional = false)
	@Column(name = "isreaded")
    Boolean isReaded = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdate")
    @Expose
    private Date createDate = new Date();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getIsReaded() {
		return isReaded;
	}

	public void setIsReaded(Boolean isReaded) {
		this.isReaded = isReaded;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
