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
 * 用户验证码记录
 * @author h j
 *
 */
@Entity
@Table(name = "send_code_frequency")
public class SendCodeFrequency {
	// Fields
    @Id
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Column(name = "phone")
    @Expose
    private String phone;
    @Column(name = "code")
    @Expose
    private String code;
    @Column(name = "ip")
    @Expose
    private String ip;
    @Column(name = "send_last_time", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Expose
    
    private Date sendLastTime = new Date();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getSendLastTime() {
		return sendLastTime;
	}
	public void setSendLastTime(Date sendLastTime) {
		this.sendLastTime = sendLastTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
}
