package com.hjedu.customer.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.google.gson.annotations.Expose;

/**
 * 
 * 用户信息表
 * 用户模块
 *
 */
@Entity
@Table(name = "user_info")
public class UserInfo {
	private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
	
    @Column(name = "true_name")
    @Expose
    private String trueName;
    
    @Column(name = "genger")
    @Expose
    private String gender;
    
    @Column(name = "email")
    @Expose
    private String email;
    
    @Column(name = "birthday", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    private Date birthday;
    
    @Column(name = "qq")
    @Expose
    private String qq;
    
    @Column(name = "pid")
    @Expose
    private String pid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
