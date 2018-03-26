package com.hjedu.course.vo;

import java.io.Serializable;
import java.util.Date;

public class LessonTypeLogVO implements Serializable{

	private String id;
	
	private String username;//用户名称
	
	private String courseName;//课程名称
	
	private String postType;//岗位类别
	
	private String groupCnStr;//部门
	
	private Date lastTime;//登录时间
	
	private Double totalClassNum;//总课时
	
	private Double finishedClassNum;//完成学时
	
	private boolean finished;//完成学时
		
	public LessonTypeLogVO(String id,String username,String courseName,String postType,String groupCnStr,Date lastTime,Double totalClassNum, Double finishedClassNum,boolean finished){
		this.id = id;
		this.username = username;
		this.courseName = courseName;
		this.postType = postType;
		this.groupCnStr = groupCnStr;
		this.lastTime = lastTime;
		this.totalClassNum = totalClassNum;
		this.finishedClassNum = finishedClassNum;
		this.finished = finished;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

	public String getGroupCnStr() {
		return groupCnStr;
	}

	public void setGroupCnStr(String groupCnStr) {
		this.groupCnStr = groupCnStr;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public Double getTotalClassNum() {
		return totalClassNum;
	}

	public void setTotalClassNum(Double totalClassNum) {
		this.totalClassNum = totalClassNum;
	}

	public Double getFinishedClassNum() {
		return finishedClassNum;
	}

	public void setFinishedClassNum(Double finishedClassNum) {
		this.finishedClassNum = finishedClassNum;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
