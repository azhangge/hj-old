package com.hjedu.course.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import com.google.gson.annotations.Expose;
/**
 * 学习计划模块
 * 学习计划定制课程
 */
@Entity
@Table(name = "course_plan")
public class CourseOfPlan implements Serializable, Comparable{

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private String id = UUID.randomUUID().toString();
    @Lob
    @Column(name = "name")
    @Expose
    private String name;
    @Column(name = "ord")
    @Expose
    private int ord;
    @Column(name = "gen_time")
    @Temporal(TemporalType.TIMESTAMP)
    @Expose
    private Date genTime = new Date();
    
    @ManyToOne
    @JoinColumn(name = "study_plan_id")
    private StudyPlan studyPlan;
    
    @Column(name = "teacher")
    @Expose
    private String teacher;
    
    @Column(name = "class_num")
    @Expose
    private Double totalClassNum;
    
    @Column(name = "if_required")
    @Expose
    private boolean ifRequired;
    
    @Transient
    private boolean selected = false;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lesson_type_id")
    @Expose
    private LessonType lessonType;

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Double getTotalClassNum() {
		return totalClassNum;
	}

	public void setTotalClassNum(Double totalClassNum) {
		this.totalClassNum = totalClassNum;
	}

	public boolean isIfRequired() {
		return ifRequired;
	}

	public void setIfRequired(boolean ifRequired) {
		this.ifRequired = ifRequired;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public Date getGenTime() {
		return genTime;
	}

	public void setGenTime(Date genTime) {
		this.genTime = genTime;
	}

	public StudyPlan getStudyPlan() {
		return studyPlan;
	}

	public void setStudyPlan(StudyPlan studyPlan) {
		this.studyPlan = studyPlan;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	@Override
    public int compareTo(Object o) {
        CourseOfPlan cq = (CourseOfPlan) o;
        if (this.getOrd() == cq.getOrd()) {
            return this.getId().compareTo(cq.getId());
        } else {
            return this.getOrd() - cq.getOrd();
        }
    }

    @Override
    public boolean equals(Object object) {
        CourseOfPlan other = (CourseOfPlan) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.huajie.model.CourseOfPlan[ name=" + name + " ]";
    }
}
