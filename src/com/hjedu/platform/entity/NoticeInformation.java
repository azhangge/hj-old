package com.hjedu.platform.entity;

import java.util.Date;

public class NoticeInformation {
	private String messageId;
	private boolean isreaded;
	private String userId;
	private Date createDate;
	private Date modifyDate;
	private String title;
	private String content;
	
	public NoticeInformation(){
	};

	public NoticeInformation(String messageId, boolean isreaded, String userId, Date createDate, Date modifyDate,
			String title, String content) {
		super();
		this.messageId = messageId;
		this.isreaded = isreaded;
		this.userId = userId;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		this.title = title;
		this.content = content;
	}

	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public boolean isIsreaded() {
		return isreaded;
	}
	public void setIsreaded(boolean isreaded) {
		this.isreaded = isreaded;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
