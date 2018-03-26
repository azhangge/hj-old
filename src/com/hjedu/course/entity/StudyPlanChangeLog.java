package com.hjedu.course.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.google.gson.annotations.Expose;
import com.hjedu.examination.entity.Examination;

/**
 * 学习计划动态记录
 * @author h j
 *
 */
@Entity
@Table(name = "SPchange_log")
public class StudyPlanChangeLog {
	StudyPlanChangeLog(){}
	public StudyPlanChangeLog(StudyPlanLog studyPlanLog,String logType){
		this.studyPlanLog = studyPlanLog;
		this.logType = logType;
	}
	@Id
    @Column(name = "id")
	@Expose
    private String id = UUID.randomUUID().toString();
	
	/**
	 * 级联更新StudyPlanLog
	 * (cascade={CascadeType.MERGE})
	 */
	@ManyToOne
	@JoinColumn(name = "study_plan_log_id")
	private StudyPlanLog studyPlanLog;
	
	@Basic(optional = false)
	@Column(name = "update_time")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date updateTime = new Date();
	
	@Basic(optional = false)
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	@Expose
	private Date createTime = new Date();
	
	/**
	 * 日志类型
	 * 0：参加学习计划
	 * 1：完成学习资料
	 * 2：完成课程
	 * 3：完成考试
	 * 4：完成学习计划
	 */
	@Column(name = "log_type")
	@Expose
    private String logType;
	
	@JoinColumn(name = "lesson_type_id")
	@Expose
    private LessonType lessonType;
	
	@JoinColumn(name = "lesson_id")
	@Expose
    private Lesson lesson;
	
	@JoinColumn(name = "exam_id")
	@Expose
	private Examination exam;
	
	/**
	 * 考试分数
	 */
	@Column(name = "exam_score")
	@Expose
	private Double examScore;
	
	/**
	 * 学时
	 */
	@Column(name = "class_num")
	@Expose
	private Double classNum;
	
	/**
	 * 课程完成百分比
	 */
	@Column(name = "completion_percent")
	@Expose
	private String completionPercent;
	
	/**
	 * 考试是否通过
	 */
	@Column(name = "if_passed")
	@Expose
	private boolean ifPassed;
	
	/**
	 * 日志类型
	 * 0：参加学习计划
	 * 1：完成学习资料
	 * 2：完成课程
	 * 3：完成考试
	 * 4：完成学习计划
	 */
	public String getLogType() {
		return logType;
	}
	
	/**
	 * 日志类型
	 * 0：参加学习计划
	 * 1：完成学习资料
	 * 2：完成课程
	 * 3：完成考试
	 * 4：完成学习计划
	 */
	public void setLogType(String logType) {
		this.logType = logType;
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Examination getExam() {
		return exam;
	}

	public void setExam(Examination exam) {
		this.exam = exam;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public StudyPlanLog getStudyPlanLog() {
		return studyPlanLog;
	}

	public void setStudyPlanLog(StudyPlanLog studyPlanLog) {
		this.studyPlanLog = studyPlanLog;
	}
	public Double getClassNum() {
		return classNum;
	}
	public void setClassNum(Double classNum) {
		this.classNum = classNum;
	}
	public String getCompletionPercent() {
		return completionPercent;
	}
	public void setCompletionPercent(String completionPercent) {
		this.completionPercent = completionPercent;
	}
	public Double getExamScore() {
		return examScore;
	}
	public void setExamScore(Double examScore) {
		this.examScore = examScore;
	}
	public boolean isIfPassed() {
		return ifPassed;
	}
	public void setIfPassed(boolean ifPassed) {
		this.ifPassed = ifPassed;
	}

}
