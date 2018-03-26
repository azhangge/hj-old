/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hjedu.course.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import com.hjedu.course.dao.ILessonTypeLogDAO;
import com.hjedu.course.dao.IStudyPlanChangeLogDAO;
import com.hjedu.course.dao.IStudyPlanLogDAO;
import com.hjedu.course.service.IStudyPlanLogService;
import com.hjedu.customer.entity.BbsUser;
import com.huajie.util.SpringHelper;

/**
 * 课程模块
 * 学习资料学习记录
 * @author h j
 *
 */
@Entity
@Table(name = "lesson_log")
public class LessonLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id=UUID.randomUUID().toString();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private BbsUser user;
    @ManyToOne
    @JoinColumn(name = "lesson_id")
    @Expose
    private Lesson lesson;
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime=new Date();
    @Column(name="score_paid")
    @Expose
    private double scorePaid=0;
    @Column(name="time_finished")
    @Expose
    private double timeFinished=0;
    @Column(name="finished")
    @Expose
    private boolean finished=false;
    @Transient
    private boolean selected = false;
    
    @Column(name="video_time")
    @Expose
    private double videoTime=0;

    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
    public double getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(double videoTime) {
		this.videoTime = videoTime;
	}

	public LessonLog() {
    }

    public LessonLog(String id) {
        this.id = id;
    }

    public LessonLog(String id, Date genTime) {
        this.id = id;
        this.genTime = genTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    
    
    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double getScorePaid() {
        return scorePaid;
    }

    public void setScorePaid(double scorePaid) {
        this.scorePaid = scorePaid;
    }

    public double getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(double timeFinished) {
        this.timeFinished = timeFinished;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
    	this.finished = finished;
    	IStudyPlanLogService dtudyPlanLogService = SpringHelper.getSpringBean("StudyPlanLogService");
    	dtudyPlanLogService.createFinishLessonLog(this);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LessonLog)) {
            return false;
        }
        LessonLog other = (LessonLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.exam.model.LessonLog[ id=" + id + " ]";
    }
    
}
