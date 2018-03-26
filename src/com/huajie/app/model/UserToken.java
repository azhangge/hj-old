package com.huajie.app.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.google.gson.annotations.Expose;
/**
 * 登陆模块
 * 用户token表
 * @author h j
 *
 */
@Entity
@Table(name = "user_token")
public class UserToken {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();;
    
    @Column(name = "token")
    @Expose
    private String token = UUID.randomUUID().toString();
    
    @Column(name = "user_id")
    @Expose
    private String user_id;
    
    @Column(name = "create_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date createTime = new Date();

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
    
}
