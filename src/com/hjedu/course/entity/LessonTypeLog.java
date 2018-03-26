/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hjedu.course.entity;

import com.google.gson.annotations.Expose;
import com.hjedu.customer.entity.BbsUser;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Noncacheable;

/**
 * 课程模块
 * 课程学习记录
 * @author h j
 *
 */
@Entity
@Table(name = "lesson_type_log")
public class LessonTypeLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id=UUID.randomUUID().toString();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private BbsUser user;
    //购买时间
    @Basic(optional = false)
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime=new Date();
    
    //最新修改时间
    @Basic(optional = false)
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date updateTime=new Date();
    
    //课程完成学时
    @Column(name="finished_class_num")
    @Expose
    private Double finishedClassNum = 0d;
    //课程是否完成
    @Column(name="finished")
    @Expose
    private boolean finished=false;
    @Transient
    private boolean selected = false;
    
    @ManyToOne
    @JoinColumn(name = "lesson_type_id")
    @Expose
    private LessonType lessonType;
    //学习次数
    @Column(name="learn_num")
    @Expose
    private Long learnNum;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @Noncacheable
    @JoinTable(name = "type_log_study_plan",
            joinColumns = {
                @JoinColumn(name = "type_log_id", referencedColumnName = "id")},
            inverseJoinColumns = {
                @JoinColumn(name = "plan_id", referencedColumnName = "id")})
    private List<StudyPlan> studyPlan;
    
    //是否必修
    @Column(name="if_required")
    @Expose
    private boolean ifRequired;
    
    //学习计划中指定学时
    @Column(name="plan_class_num")
    @Expose
    private double planClassNum;
    
    //学习计划中指定需要完成学时
    @Column(name="pass_class_num")
    @Expose
    private double passClassNum;
    
    @Column(name = "business_id")
	private String businessId;
    
    public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
    
	public LessonTypeLog() {
	}

    public LessonTypeLog(LessonType ltp, BbsUser user) {
        this.lessonType = ltp;
        this.user = user;
    }

	public LessonTypeLog(LessonType ltp, BbsUser user, List<StudyPlan> studyPlan,boolean ifRequired) {
		this.studyPlan = studyPlan;
		this.lessonType = ltp;
        this.user = user;
        this.ifRequired = ifRequired;
        this.genTime = new Date();
	}

	public boolean isIfRequired() {
		return ifRequired;
	}

	public void setIfRequired(boolean ifRequired) {
		this.ifRequired = ifRequired;
	}

	public List<StudyPlan> getStudyPlan() {
		return studyPlan;
	}

	public void setStudyPlan(List<StudyPlan> studyPlan) {
		this.studyPlan = studyPlan;
	}

	public double getPlanClassNum() {
		return planClassNum;
	}

	public void setPlanClassNum(int planClassNum) {
		this.planClassNum = planClassNum;
	}

	public double getPassClassNum() {
		return passClassNum;
	}

	public void setPassClassNum(int passClassNum) {
		this.passClassNum = passClassNum;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getLearnNum() {
		return learnNum;
	}

	public void setLearnNum(Long learnNum) {
		this.learnNum = learnNum;
	}

	public Double getFinishedClassNum() {
		return finishedClassNum;
	}

	public void setFinishedClassNum(Double finishedClassNum) {
		this.finishedClassNum = finishedClassNum;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public BbsUser getUser() {
        return user;
    }

    public void setUser(BbsUser user) {
        this.user = user;
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LessonTypeLog)) {
            return false;
        }
        LessonTypeLog other = (LessonTypeLog) object;
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
